/*******************************************************************************
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
 *
 *******************************************************************************/

package com.liferay.ide.xml.search.ui;

import com.liferay.ide.core.util.CoreUtil;
import com.liferay.ide.portlet.core.dd.PortletDescriptorHelper;

import java.io.ByteArrayInputStream;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * @author Terry Jia
 */
public class AddNewLanguageFileMarkerResolution extends AbstractLanguagePropertiesMarkerResolution
{

    private String languageFilePackage = "content";

    private String languageFileName = "Language";

    public AddNewLanguageFileMarkerResolution( IMarker marker )
    {
        super( marker );
    }

    private void checkResourceBundleElement( IProject project )
    {
        PortletDescriptorHelper portletDescriptorHelper = new PortletDescriptorHelper( project );

        String[] resouceBundles = portletDescriptorHelper.getAllResourceBundles();

        if( resouceBundles.length == 0 )
        {
            portletDescriptorHelper.addResourceBundle( languageFilePackage + "." + languageFileName );
        }
        else
        {
            for( String resouceBundle : resouceBundles )
            {
                if( !CoreUtil.isNullOrEmpty( resouceBundle ) )
                {
                    // TODO does this handle bundles like foo.bar.myresources.content.Language?
                    String[] paths = resouceBundle.split( "\\." );

                    if( paths.length == 2 )
                    {
                        languageFilePackage = paths[0];
                        languageFileName = paths[1];
                    }
                    else if( paths.length == 1 )
                    {
                        languageFilePackage = "";
                        languageFileName = paths[0];
                    }

                    break;
                }
            }
        }
    }

    @Override
    public String getLabel()
    {
        return "Create a new default resource bundle add it to portlet.xml.";
    }

    public Image getImage()
    {
        final URL url = LiferayXMLSearchUI.getDefault().getBundle().getEntry( "/icons/resource-bundle-new.png" );

        return ImageDescriptor.createFromURL( url ).createImage();
    }

    @Override
    protected void resolve( IMarker marker )
    {
        final IProject project = marker.getResource().getProject();

        final String message = marker.getAttribute( IMarker.MESSAGE, "" );

        if( message.equals( "" ) || project == null )
        {
            return;
        }

        try
        {
            checkResourceBundleElement( project );

            // TODO on maven projects this should be resources folder
            final IFolder folder = CoreUtil.getFirstSrcFolder( project ).getFolder( languageFilePackage );

            if( !folder.exists() )
            {
                CoreUtil.makeFolders( folder );
            }

            final IFile languageFile = folder.getFile( languageFileName + ".properties" );

            String languageKey = getLanguageKey( message );
            String languageMessage = getDefaultLanguageMessage( languageKey );
            String languagePropertyLine = languageKey + "=" + languageMessage + "\n";

            if( !languageFile.exists() )
            {
                IFolder parent = (IFolder) languageFile.getParent();

                CoreUtil.prepareFolder( parent );

                languageFile.create(
                    new ByteArrayInputStream( languagePropertyLine.getBytes( "UTF-8" ) ), IResource.FORCE, null );
            }
            else
            {
                String contents = CoreUtil.readStreamToString( languageFile.getContents() );

                StringBuffer sb = new StringBuffer();

                sb.append( contents );
                sb.append( getLanguageKey( message ) );
                sb.append( "=" );
                sb.append( languageMessage );
                sb.append( "\n" );

                languageFile.setContents(
                    new ByteArrayInputStream( sb.toString().getBytes( "UTF-8" ) ), IResource.FORCE,
                    new NullProgressMonitor() );
            }

            openEditor( languageFile );
        }
        catch( Exception e )
        {
            LiferayXMLSearchUI.logError( e );
        }
    }

}
