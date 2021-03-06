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

package com.liferay.ide.upgrade.problems.core.internal.liferay70;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;

import com.liferay.ide.upgrade.problems.core.FileMigrator;
import com.liferay.ide.upgrade.problems.core.FileSearchResult;
import com.liferay.ide.upgrade.problems.core.JavaFile;
import com.liferay.ide.upgrade.problems.core.internal.JavaFileMigrator;

/**
 * @author Gregory Amerson
 */
@Component(property = {
	"file.extensions=java,jsp,jspf", "problem.title=Removed Asset Tag Properties",
	"problem.summary=The Asset Tag Properties have been removed. The service no longer exists and the Asset Tag " +
		"Service API no longer has this parameter. The behavior associated with tag properties in the Asset " +
			"Publisher and XSL portlets has also been removed.",
	"problem.tickets=LPS-52588", "problem.section=#removed-asset-tag-properties", 
	"version=7.0"
},
	service = FileMigrator.class)
public class AssetTagProperties extends JavaFileMigrator {

	@Override
	protected List<FileSearchResult> searchFile(File file, JavaFile javaFileChecker) {
		List<FileSearchResult> searchResults = new ArrayList<>();

		// all imports from AssetTagPropertyLocalServiceUtil and
		// AssetTagPropertyServiceUtil

		FileSearchResult searchResult = javaFileChecker.findImport(
			"com.liferay.portlet.asset.service.AssetTagPropertyLocalServiceUtil");

		if (searchResult != null) {
			searchResults.add(searchResult);
		}

		searchResult = javaFileChecker.findImport("com.liferay.portlet.asset.service.AssetTagPropertyServiceUtil");

		if (searchResult != null) {
			searchResults.add(searchResult);
		}

		// all calls on AssetTagPropertyLocalServiceUtil

		List<FileSearchResult> localInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagPropertyLocalServiceUtil", "*", null);

		searchResults.addAll(localInvocations);

		// all calls on AssetTagPropertyServiceUtil

		List<FileSearchResult> serviceInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagPropertyServiceUtil", "*", null);

		searchResults.addAll(serviceInvocations);

		// all calls on methods with String[] tagProperties parameter
		// AssetTagLocalServiceUtil java.lang.String[] tagProperties

		List<FileSearchResult> tagServiceLocalUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagLocalServiceUtil", "addTag", new String[] {"long", "String", "String[]", "ServiceContext"});

		searchResults.addAll(tagServiceLocalUtilInvocations);

		tagServiceLocalUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagLocalServiceUtil", "search", new String[] {"long", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceLocalUtilInvocations);

		tagServiceLocalUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagLocalServiceUtil", "search", new String[] {"long[]", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceLocalUtilInvocations);

		tagServiceLocalUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagLocalServiceUtil", "updateTag",
			new String[] {"long", "long", "String", "String[]", "ServiceContext"});

		searchResults.addAll(tagServiceLocalUtilInvocations);

		// AssetTagServiceUtil java.lang.String[] tagProperties

		List<FileSearchResult> tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "addTag", new String[] {"String", "String[]", "ServiceContext"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "getTags", new String[] {"long", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "getTags", new String[] {"long[]", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "getTagsCount", new String[] {"long", "String", "String[]"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "search", new String[] {"long", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "search", new String[] {"long[]", "String", "String[]", "int", "int"});

		searchResults.addAll(tagServiceUtilInvocations);

		tagServiceUtilInvocations = javaFileChecker.findMethodInvocations(
			null, "AssetTagServiceUtil", "updateTag", new String[] {"long", "String", "String[]", "ServiceContext"});

		searchResults.addAll(tagServiceUtilInvocations);

		return searchResults;
	}

}