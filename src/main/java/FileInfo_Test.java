import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.plugin.PlugIn;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlTag.HEAD;

public class FileInfo_Test implements PlugIn {
      public void run(String arg) {
        FileInfo fi = new FileInfo();
        fi.width = 256;
        fi.height = 254;
        fi.offset = 768;
        fi.fileName = "130318_cg38_0R2L_afterIVinsulin-1.tif";

        fi.directory = "/Users/sachamaire/Desktop/";
          fi.directory = " /Users/sachamaire/Desktop/130318_cg38_0R2L_afterIVinsulin-1.tif";
          new FileOpener(fi).open();

      }
}


