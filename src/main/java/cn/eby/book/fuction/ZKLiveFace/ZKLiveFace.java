package cn.eby.book.fuction.ZKLiveFace;

public class ZKLiveFace {

    public static byte[] GetTemplate(String FileName, long context)
    {
        byte[] bufRet = null;
        byte[] bufRaw = null;
        int nWidth[] = new int[1];
        int nHeight[] = new int[1];
        int nSize[] = new int[1];
        //先获取长度?
        int nRet = ZKLiveFaceService.loadImage(FileName, null, nWidth, nHeight, nSize);
        if (0 != nRet)
        {
            System.out.println("LoadImage [" + FileName + "] failed, retCode=" + nRet);
            return bufRet;
        }
        bufRaw = new byte[nSize[0]];
        nRet = ZKLiveFaceService.loadImage(FileName, bufRaw, nWidth, nHeight, nSize);
        if (0 != nRet)
        {
            System.out.println("LoadImage [" + FileName + "] failed, retCode=" + nRet);
            return bufRet;
        }
        int detectedFaces[] = new int[1];
        nRet = ZKLiveFaceService.detectFaces(context, bufRaw, nWidth[0], nHeight[0], detectedFaces);
        if (0 != nRet || detectedFaces[0] <= 0)
        {
            System.out.println("DetectFaces [" + FileName + "] failed, retCode=" + nRet);
            return bufRet;
        }
        long faceContext[] = new long[1];
        nRet = ZKLiveFaceService.getFaceContext(context, 0, faceContext);
        if (0 != nRet || 0 == faceContext[0])
        {
            System.out.println("GetFaceContext [" + FileName + "] failed, retCode=" + nRet);
            return bufRet;
        }
        byte bufTmp[] = new byte[1024*8];
        int size[] = new int[1];
        int resverd[] = new int[1];
        size[0] = 1024*8;
        nRet = ZKLiveFaceService.extractTemplate(faceContext[0], bufTmp, size, resverd);
        if (0 == nRet && size[0] > 0)
        {
            bufRet = new byte[size[0]];
            System.arraycopy(bufTmp, 0, bufRet, 0, size[0]);
        }
        else
        {
            System.out.println("ExtractTemplate [" + FileName + "] failed, retCode=" + nRet);
        }
        System.out.println("face context=" + faceContext[0]);
        ZKLiveFaceService.closeFaceContext(faceContext[0]);
        return bufRet;
    }


    public static void main(String[] args)
    {
        int retCode = 0;
        long instanceContext = 0;
        long[] retVal = new long[1];
        //JOptionPane.showMessageDialog( null,"插入失败!");

        byte[] version = new byte[256];
        int[] size = new int[1];
        size[0] = 256;
        if (0 == ZKLiveFaceService.version(version, size))
        {
            System.out.println("lasterror:" + new String(version, 0, size[0]));
        }

        if (0 != (retCode = ZKLiveFaceService.init(retVal)))
        {
            System.out.println("Init ZKLiveFace failed, code=" + retCode);

            byte[] lasterror = new byte[256];
            int[] lasterrorLen = new int[1];
            lasterrorLen[0] = 256;
            retCode = ZKLiveFaceService.getLastError(0,lasterror,lasterrorLen);

            System.out.println("lasterror:" + new String(lasterror, 0, lasterrorLen[0]));

            byte[] hwid = new byte[128];
            int[] retLen = new int[1];
            retLen[0] = 128;
            if (0 == (retCode = ZKLiveFaceService.getHardwareId(hwid, retLen)))
            {
                System.out.println("hardwareid:" + new String(hwid, 0, retLen[0]));
            }
            else
            {
                System.out.println("Get hardware id failed, code=" + retCode);
            }
            return;
        }
        instanceContext = retVal[0];
        retCode = ZKLiveFaceService.init(retVal);
        System.out.println("init, retcode=" + retCode + ",retVal=" + retVal[0]);
        long nTickstart = System.currentTimeMillis();
        //请您实际的照片路径来填写.
        byte[] regTmp = GetTemplate("C:\\Users\\Administrator\\Desktop\\x64\\pic\\reg.jpg", instanceContext);
        byte[] verTmp = GetTemplate("C:\\Users\\Administrator\\Desktop\\x64\\pic\\ver.jpg", instanceContext);


        if (null != regTmp && null != verTmp)
        {
            int score[] = new int[1];

            //int ret = ZKLiveFaceService.dbAdd(instanceContext, "1g2", regTmp);

            //  System.out.println("ret1=" + ret);
            // ret = ZKLiveFaceService.dbDel(instanceContext, "1g2");
            //  System.out.println("ret2=" + ret);
            // byte[] regTmp1 = new byte[1024*8];

            //1:1
            retCode = ZKLiveFaceService.verify(instanceContext, regTmp, verTmp, score);
            if (0 == retCode)
            {
                System.out.println("verify succ, score=" + score[0] + ",use time=" + (System.currentTimeMillis() - nTickstart));
            }
            else
            {
                System.out.println("verify fail, retCode=" + retCode + ",use time=" + (System.currentTimeMillis() - nTickstart));
            }

            //1:N
            retCode = ZKLiveFaceService.dbAdd(instanceContext, "人员1", regTmp);
            if(0 == retCode)
            {
                byte [] faceid = new byte[2048];
                int [] nscore = new int[1];
                int [] maxcnt = new int[1];
                maxcnt[0] = 1;
                //最小匹配分数
                int minscore = 84;
                int maxscore = 100;
                retCode = ZKLiveFaceService.dbIdentify(instanceContext,regTmp,faceid,nscore,maxcnt,minscore,maxscore);
                if (0 == retCode)
                {
                    String strfaceid = new String(faceid);
                    System.out.println("dbIdentify succ, score=" + nscore[0] +"faceid = "+strfaceid+ ",use time=" + (System.currentTimeMillis() - nTickstart));
                }
                else
                {
                    System.out.println("verify fail, retCode=" + retCode + ",use time=" + (System.currentTimeMillis() - nTickstart));
                }
            }


        }
        else
        {
            System.out.println("get reg/ver template failed!");
        }
        //  }

        ZKLiveFaceService.terminate(instanceContext);
        instanceContext = 0;

    }

}
