package com.jecelyin.editor.v2.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.timecat.component.commonsdk.utils.override.L;
import com.timecat.component.commonsdk.utils.utils.SysUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Jecelyin Peng <jecelyin@gmail.com>
 */
public class BundlesUtils {
    public static File getBundlesDir(Context context) {
        return new File(SysUtils.getCacheDir(context), "Bundles");
    }

    public static void unzipBundles(Context context) throws IOException {
        File cacheDir = SysUtils.getCacheDir(context);
        File okFile = new File(cacheDir, ".bundles_unzip_ok");
        if(L.debug && okFile.isFile())
            return;
        AssetManager assetManager = context.getAssets();
        BufferedReader reader = null;
        InputStream in = null;
        OutputStream out = null;
        try {
            reader = new BufferedReader(new InputStreamReader(assetManager.open("assets.index")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ( (mLine = reader.readLine()) != null ) {
                //process line
                if(!mLine.startsWith("Bundles/"))
                    continue;

                try {
                    in = assetManager.open(mLine);
                    File outFile = new File(cacheDir, mLine);
                    File path = outFile.getParentFile();
                    if(!path.isDirectory() && !path.mkdirs()) {
                        L.e("can't create dir: "+path.getPath());
                        continue;
                    }
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch(IOException e) {
                    throw e;
                }
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            }
            okFile.createNewFile();
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

    }
    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[12024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
