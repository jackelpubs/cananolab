package gov.nih.nci.calab.service.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ActionUtil
{
    public void writeBinaryStream(File file, HttpServletResponse response) throws Exception
    {
        if (file == null || response == null)
        {
            throw new Exception("Unable to write file to HttpServletResponse: " +
                "Either pathName or response is null.");
        }
        try
        {
            // set a non-standard content type to force brower to open Save As dialog
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            ServletOutputStream sos = response.getOutputStream();
            BufferedInputStream bis =  new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024 * 10]; // ~= 10KB
            for (int len = -1; (len = bis.read(buf)) != -1; )
            {
                sos.write(buf, 0, len);
            }
            response.setContentLength((int)file.length());
            sos.flush(); // let the Servlet container handle closing of this ServletOutputStream
            bis.close();
        }
        catch (Exception e)
        {
            throw new Exception("Unable to write file to client, exception is " + e.toString());
        }

    }
}