package com.jecelyin.editor.v2.utils;

import android.webkit.MimeTypeMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/6/21
 * @description null
 * @usage null
 */

public class MimeTypes {

  private Map<String, String> mMimeTypes;
  private Map<String, Integer> mIcons;

  private static MimeTypes instance = null;

  public static MimeTypes getInstance() {
    if(instance == null)
      instance = new MimeTypes();
    return instance;
  }

  public MimeTypes() {
    mMimeTypes = new HashMap<String, String>();
    mIcons = new HashMap<String, Integer>();
  }

  /*
   * I think the type and extension names are switched (type contains .png,
   * extension contains x/y), but maybe it's on purpouse, so I won't change
   * it.
   */
  public void put(String type, String extension, int icon) {
    put(type, extension);
    mIcons.put(extension, icon);
  }

  public void put(String type, String extension) {
    // Convert extensions to lower case letters for easier comparison
    extension = extension.toLowerCase();

    mMimeTypes.put(type, extension);
  }

  /**
   * Gets the extension of a file name, like ".png" or ".jpg".
   *
   * @param uri
   * @return Extension including the dot("."); "" if there is no extension;
   *         null if uri was null.
   */
  public static String getExtension(String uri) {
    if (uri == null) {
      return null;
    }

    int dot = uri.lastIndexOf(".");
    if (dot >= 0) {
      return uri.substring(dot);
    } else {
      // No extension.
      return "";
    }
  }

  public String getMimeType(String filename) {

    String extension = getExtension(filename);

    // Let's check the official map first. Webkit has a nice
    // extension-to-MIME map.
    // Be sure to remove the first character from the extension, which is
    // the "." character.
    if(extension == null) return null;
    if (extension.length() > 0) {
      String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
          extension.substring(1));

      if (webkitMimeType != null) {
        // Found one. Let's take it!
        return webkitMimeType;
      }
    }

    // Convert extensions to lower case letters for easier comparison
    extension = extension.toLowerCase();

    String mimetype = mMimeTypes.get(extension);

    if (mimetype == null)
      mimetype = "*/*";

    return mimetype;
  }

}
