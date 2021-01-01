/**
 * Copyright (C) 2015-2021 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.servlet.dump;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.http.HttpHeaderMap;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.file.FilenameHelper;
import com.helger.datetime.util.PDTIOHelper;
import com.helger.phase4.config.AS4Configuration;
import com.helger.phase4.dump.AbstractAS4OutgoingDumperWithHeaders;
import com.helger.phase4.dump.IAS4OutgoingDumper;

/**
 * File based implementation of {@link IAS4OutgoingDumper}
 *
 * @author Philip Helger
 * @since 0.9.3
 */
public class AS4OutgoingDumperFileBased extends AbstractAS4OutgoingDumperWithHeaders
{
  public static interface IFileProvider
  {
    @Nonnull
    File getFile (@Nonnull @Nonempty String sMessageID, @Nonnegative int nTry);

    @Nonnull
    static String getFilename (@Nonnull @Nonempty final String sMessageID, @Nonnegative final int nTry)
    {
      return PDTIOHelper.getCurrentLocalDateTimeForFilename () +
             "-" +
             FilenameHelper.getAsSecureValidASCIIFilename (sMessageID) +
             "-" +
             nTry +
             ".as4out";
    }
  }

  /**
   * The default relative path for outgoing messages.
   */
  public static final String DEFAULT_BASE_PATH = "outgoing/";
  private static final Logger LOGGER = LoggerFactory.getLogger (AS4OutgoingDumperFileBased.class);

  private final IFileProvider m_aFileProvider;

  /**
   * Default constructor. Writes the files to the AS4 configured data path +
   * {@link #DEFAULT_BASE_PATH}.
   *
   * @see AS4Configuration#getDumpBasePathFile()
   */
  public AS4OutgoingDumperFileBased ()
  {
    this ( (sMessageID, nTry) -> new File (AS4Configuration.getDumpBasePathFile (),
                                           DEFAULT_BASE_PATH + IFileProvider.getFilename (sMessageID, nTry)));
  }

  /**
   * Constructor with a custom file provider.
   *
   * @param aFileProvider
   *        The file provider that defines where to store the files. May not be
   *        <code>null</code>.
   */
  public AS4OutgoingDumperFileBased (@Nonnull final IFileProvider aFileProvider)
  {
    ValueEnforcer.notNull (aFileProvider, "FileProvider");
    m_aFileProvider = aFileProvider;
  }

  @Override
  protected OutputStream openOutputStream (@Nonnull @Nonempty final String sMessageID,
                                           @Nullable final HttpHeaderMap aCustomHeaders,
                                           @Nonnegative final int nTry) throws IOException
  {
    final File aResponseFile = m_aFileProvider.getFile (sMessageID, nTry);
    if (LOGGER.isInfoEnabled ())
      LOGGER.info ("Logging outgoing AS4 message to '" + aResponseFile.getAbsolutePath () + "'");
    return FileHelper.getBufferedOutputStream (aResponseFile);
  }

  /**
   * Create a new instance for the provided directory.
   *
   * @param aBaseDirectory
   *        The absolute directory to be used. May not be <code>null</code>.
   * @return The created dumper. Never <code>null</code>.
   * @since 0.10.2
   */
  @Nonnull
  public static AS4OutgoingDumperFileBased createForDirectory (@Nonnull final File aBaseDirectory)
  {
    ValueEnforcer.notNull (aBaseDirectory, "BaseDirectory");
    return new AS4OutgoingDumperFileBased ( (sMessageID, nTry) -> new File (aBaseDirectory, IFileProvider.getFilename (sMessageID, nTry)));
  }
}
