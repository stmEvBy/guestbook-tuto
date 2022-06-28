package com.liferay.docs.guestbook.portlet.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.portlet.constants.GuestbookPortletKeys;
import com.liferay.docs.guestbook.service.GuestbookEntryLocalService;
import com.liferay.docs.guestbook.service.GuestbookLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;

/**
 * @author Evoleen
 */
@Component(
	immediate = true,
	property = {
			"com.liferay.portlet.display-category=category.social",
		      "com.liferay.portlet.instanceable=false",
		      "com.liferay.portlet.scopeable=true",
		      "javax.portlet.display-name=Guestbook",
		      "javax.portlet.expiration-cache=0",
		      "javax.portlet.init-param.template-path=/",
		      "javax.portlet.init-param.view-template=/guestbook/view.jsp",
		      "javax.portlet.resource-bundle=content.Language",
		      "javax.portlet.name=" + GuestbookPortletKeys.GUESTBOOK,
		      "javax.portlet.security-role-ref=power-user,user",
		      "javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class GuestbookPortlet extends MVCPortlet {
	@Reference
	private GuestbookEntryLocalService _guestbookEntryLocalService;

	@Reference
	private GuestbookLocalService _guestbookLocalService;
	
	public void addEntry(ActionRequest request, ActionResponse response)
            throws PortalException {

        ServiceContext serviceContext = ServiceContextFactory.getInstance(
            GuestbookEntry.class.getName(), request);

        String userName = ParamUtil.getString(request, "name");
        String email = ParamUtil.getString(request, "email");
        String message = ParamUtil.getString(request, "message");
        long guestbookId = ParamUtil.getLong(request, "guestbookId");
        long entryId = ParamUtil.getLong(request, "entryId");

    if (entryId > 0) {

        try {

            _guestbookEntryLocalService.updateGuestbookEntry(
                serviceContext.getUserId(), guestbookId, entryId, userName,
                email, message, serviceContext);

            response.setRenderParameter(
                "guestbookId", Long.toString(guestbookId));

        }
        catch (Exception e) {
            System.out.println(e);

            PortalUtil.copyRequestParameters(request, response);

            response.setRenderParameter(
                "mvcPath", "/guestbook/edit_entry.jsp");
        }

    }
    else {

        try {
            _guestbookEntryLocalService.addGuestbookEntry(
                serviceContext.getUserId(), guestbookId, userName, email,
                message, serviceContext);

            response.setRenderParameter(
                "guestbookId", Long.toString(guestbookId));

        }
        catch (Exception e) {
            System.out.println(e);

            PortalUtil.copyRequestParameters(request, response);

            response.setRenderParameter(
                "mvcPath", "/guestbook/edit_entry.jsp");
        }
    }
}

}
