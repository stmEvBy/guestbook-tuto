/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.docs.guestbook.service.impl;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.liferay.docs.guestbook.exception.GuestbookEntryEmailException;
import com.liferay.docs.guestbook.exception.GuestbookEntryMessageException;
import com.liferay.docs.guestbook.exception.GuestbookEntryNameException;
import com.liferay.docs.guestbook.exception.GuestbookNameException;
import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.model.GuestbookEntry;
import com.liferay.docs.guestbook.service.base.GuestbookLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author liferay
 */
@Component(
	property = "model.class.name=com.liferay.docs.guestbook.model.Guestbook",
	service = AopService.class
)
public class GuestbookLocalServiceImpl extends GuestbookLocalServiceBaseImpl {
	public Guestbook addGuestbook(long userId, String name,
			ServiceContext serviceContext) throws PortalException {

		long groupId = serviceContext.getScopeGroupId();

		User user = userLocalService.getUserById(userId);

		Date now = new Date();

		validate(name);

		long guestbookId = counterLocalService.increment();

		Guestbook guestbook = guestbookPersistence.create(guestbookId);

		guestbook.setUuid(serviceContext.getUuid());
		guestbook.setUserId(userId);
		guestbook.setGroupId(groupId);
		guestbook.setCompanyId(user.getCompanyId());
		guestbook.setUserName(user.getFullName());
		guestbook.setCreateDate(serviceContext.getCreateDate(now));
		guestbook.setModifiedDate(serviceContext.getModifiedDate(now));
		guestbook.setName(name);
		guestbook.setExpandoBridgeAttributes(serviceContext);

		guestbookPersistence.update(guestbook);

		return guestbook;
	}
	
	public List<Guestbook> getGuestbooks(long groupId) {

		return guestbookPersistence.findByGroupId(groupId);
	}

	public List<Guestbook> getGuestbooks(long groupId, int start, int end,
			OrderByComparator<Guestbook> obc) {

		return guestbookPersistence.findByGroupId(groupId, start, end, obc);
	}

	public List<Guestbook> getGuestbooks(long groupId, int start, int end) {

		return guestbookPersistence.findByGroupId(groupId, start, end);
	}

	public int getGuestbooksCount(long groupId) {

		return guestbookPersistence.countByGroupId(groupId);
	}
	
	protected void validate(String name) throws PortalException {
		if (Validator.isNull(name)) {
			throw new GuestbookNameException();
		}
	}
	
	public GuestbookEntry updateGuestbookEntry(long userId, long guestbookId,
			long entryId, String name, String email, String message,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		Date now = new Date();

		validate(name, email, message);

		GuestbookEntry entry =
			guestbookEntryPersistence.findByPrimaryKey(entryId);

		User user = userLocalService.getUserById(userId);

		entry.setUserId(userId);
		entry.setUserName(user.getFullName());
		entry.setModifiedDate(serviceContext.getModifiedDate(now));
		entry.setName(name);
		entry.setEmail(email);
		entry.setMessage(message);
		entry.setExpandoBridgeAttributes(serviceContext);

		guestbookEntryPersistence.update(entry);

		// Integrate with Liferay frameworks here.

		return entry;
	}
	
	public GuestbookEntry deleteGuestbookEntry(GuestbookEntry entry)
			throws PortalException {

			guestbookEntryPersistence.remove(entry);

			return entry;
		}

		public GuestbookEntry deleteGuestbookEntry(long entryId) throws PortalException {

			GuestbookEntry entry =
				guestbookEntryPersistence.findByPrimaryKey(entryId);

			return deleteGuestbookEntry(entry);
		}
		
		public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId) {
			return guestbookEntryPersistence.findByG_G(groupId, guestbookId);
		}

		public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId,
				int start, int end) throws SystemException {

			return guestbookEntryPersistence.findByG_G(groupId, guestbookId, start,
					end);
		}

		public List<GuestbookEntry> getGuestbookEntries(long groupId, long guestbookId,
				int start, int end, OrderByComparator<GuestbookEntry> obc) {

			return guestbookEntryPersistence.findByG_G(groupId, guestbookId, start,
					end, obc);
		}

		public GuestbookEntry getGuestbookEntry(long entryId) throws PortalException {
			return guestbookEntryPersistence.findByPrimaryKey(entryId);
		}

		public int getGuestbookEntriesCount(long groupId, long guestbookId) {
			return guestbookEntryPersistence.countByG_G(groupId, guestbookId);
		}
		
		protected void validate(String name, String email, String entry)
			throws PortalException {

			if (Validator.isNull(name)) {
				throw new GuestbookEntryNameException();
			}

			if (!Validator.isEmailAddress(email)) {
				throw new GuestbookEntryEmailException();
			}

			if (Validator.isNull(entry)) {
				throw new GuestbookEntryMessageException();
			}
		}
}