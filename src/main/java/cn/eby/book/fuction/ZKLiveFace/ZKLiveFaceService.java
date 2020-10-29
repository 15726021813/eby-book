package cn.eby.book.fuction.ZKLiveFace;



public class ZKLiveFaceService {

    static {
        System.load("dll\\msvcr100.dll");
        System.load("dll\\msvcp100.dll");
        System.load("dll\\opencv_core2410.dll");
        System.load("dll\\opencv_highgui2410.dll");
        System.load("dll\\libfacealgo.dll");
        System.load("dll\\libsearch.dll");
        System.load("dll\\FaceArcWin.dll");
        System.load("dll\\ZKLiveFace.dll");
    }

    /**
     * Get lasterror
     * 返回最近一次的错误信息
     * @param context      算法实例指针(允许传NULL)，当传的不为NULL时为该实例的最近一次错误
     *                     (错误码为11时可调用该接口获取错误描述)
     * @param lasterror    错误信息（建议预分配512字节，足够使用）
     * @param size         [in]:version内存大小(字节数)
     *                     [out]:实际返回lasterror长度
     * @return error code  该接口返回失败，一般错误原因是分配的内存不足)
     */
    public native static int getLastError(long context,byte[] lasterror, int[] size);


    /**
     * Get hardware id
     * 获取机器码
     * @param hwid  返回机器码（建议预分配256字节，足够使用）
     * @param size  [in]:hwId内存大小(字节数)
     *              [out]:实际返回hwId长度
     * @return error code   错误码(见附录1)
     */
    public native static int getHardwareId(byte[] hwid, int[] size);

    /**
     * get the version of sdk
     * 获取版本号
     * @param version       返回版本号(建议预分配32字节以上)
     * @return error code   [in]:version内存大小(字节数)
     *                      [out]:实际返回version长度
     */
    public native static int version(byte[] version, int[] size);

    /**
     * Load image file (convert to raw image)
     * 加载图片文件并转换为BGR格式图像数据
     * @param FileName  文件全路径(支持的格式有:png, bmp, tif, jpg)
     * @param rawImage  BGR格式图像数据，请参考备注1的说明，得到要预分配的内存长度。返回原始图数据
     * @param width     返回图像宽
     * @param height    返回图像高
     * @param size      [in]:rawImage分配内存大小
     *                  [out]:实际返回rawImage数据长度
     * @return error code   错误码(见附录1)
     */
    public native static int loadImage(String fileName, byte[] rawImage, int[] width, int[] height, int[] size);


    /**
     * Initialize
     * 初始化算法库
     * @param context 返回算法实例指针(context[0])
     * @return error code   错误码(见附录1)
     */
    public native static int init(long[] context);

    /**
     * Set parameter
     * 设置参数
     * @param context 算法实例指针
     * @param code    参数代码（见附录2）
     * @param value   参数值
     * @param size    数据长度(字节数)
     * @return error code   错误码(见附录1)
     */
    public native static int setParameter(long context, int code, byte[] value, int size);

    /**
     * Get parameter
     * 获取参数
     * @param context 算法实例指针
     * @param code    参数代码(见附录2)
     * @param value   参数值
     * @param size    [in]:value分配数据长度
     *                [out]:实际返回参数数据长度
     * @return error code
     */
    public native static int getParameter(long context, int code, byte[] value, int[] size);

    /**
     * Detect faces
     * 探测人脸
     * @param context       算法实例指针
     * @param rawImage      原始图像数据(见loadImage)
     * @param width         图像宽
     * @param height        图像高
     * @param detectedFaces 探测到人脸数(<=最大探测人脸数(PARAM_CODE_MAX_FACE))
     * @return error code   错误码(见附录1)
     * 1、rawImage默认为：BGR图像位深度为24位的原始图像数据
     * 2、该接口为非线程安全接口。
     *
     */
    public native static int detectFaces(long context, byte[] rawImage, int width, int height, int[] detectedFaces);

    /**
     * Finalize
     * 释放算法资源
     * @param context       算法实例指针
     * @return error code   错误码(见附录1)
     */
    public native static int terminate(long context);

    /**
     * Get the face context by face index
     * 获取指定人脸实例指针
     * @param context       算法实例指针
     * @param faceIdx       人脸索引(见ZKFace_DetectFaces，0~[detectedFaces-1])
     * @param faceContext   返回人脸实例指针
     * @return err code`
     */
    public native static int getFaceContext(long context, int faceIdx, long[] faceContext);

    /**
     * Get the crop image
     * 获取小图像数据（RGB图像位深度为24位图像数据）
     * @param faceContext     人脸实例指针
     * @param cropWidth       缩略图图像宽度
     * @param cropHeight      缩略图图像高度
     * @param cropLength      [in]:rawCroppedImage内存大小
     *                        [out]:实际返回图像数据大小
     * @param rawCroppedImage 返回图像数据(RGB图像位深度为24位图像数据)
     * @return error code
     */
    public native static int getCropImageData(long faceContext, int[] cropWidth, int[] cropHeight, int[] cropLength, byte[] rawCroppedImage);

    /**
     * Get the feature of face
     * 获取特征
     * @param faceContext   人脸实例指针
     * @param featureID     特征ID（见附录4）
     * @param pX            返回X坐标
     * @param pY            返回Y坐标
     * @param score         返回分数(预留参数)
     * @return error code    错误码(见附录1)
     */
    public native static int getFaceFeature(long faceContext, int featureID, int[] pX, int[] pY, int[] score);

    /**
     * Get the ICAO feature of face
     * 获取ICao特征
     * @param faceContext 人脸实例指针
     * @param featureID   特征ID（见附录3）
     * @param score       返回对应的特征值
     * @return error code 错误码(见附录1)
     */
    public native static int getFaceICaoFeature(long faceContext, int featureID, int[] score);

    /**
     * Extract template
     * 提取人脸模板
     * @param faceContext 人脸实例指针
     * @param template    （建议预分配2048个字节，足够使用）人脸模板
     * @param size         [in]:template内存分配大小
     *                     [out]:实际返回template数据长度
     * @param resverd      该参数为预留参数
     * @return error code
     */
    public native static int extractTemplate(long faceContext, byte[] template, int[] size, int[] resverd);

    /**
     * get the rect of rect
     * 获取探测到人脸的矩形框
     * @param context   人脸实例指针
     * @param points    矩形框四个坐标点p0.x p0.y p1.x p1.y p2.x p2.y p3.x p3.y顺序排列（顺时针方向）
     * @param cntPx     points数组大小(8)
     * @return
     */
    public native static int getFaceRect(long faceContext, int[] points, int cntPx);

    /**
     * Close face context
     * 释放人脸实例对象
     * @param faceContext 人脸实例指针
     * @return error code
     */
    public native static int closeFaceContext(long faceContext);

    /**
     * Verify template
     * 人脸1:1比对
     * @param context     算法实例指针
     * @param regTemplate 登记模板
     * @param verTemplate 比对模板
     * @param score       返回分数
     *                    1、默认1:1的比对阀值为60。超过即为比对成功。
     *                    2、比对分数范围：0~120
     *                    3、人证合一时阀值可以设置为：55~60 。普通照片时：1：1人脸阀值可以设置为：80
     * @return error code
     */
    public native static int verify(long context, byte[] regTemplate, byte[] verTemplate, int[] score);

    /**
     * Add a reg-template to memory db
     * 添加人脸模板到默认的1:N高速缓冲区
     * @param context     算法实例指针
     * @param faceID      人脸ID
     * @param regTemplate 登记模板
     * @return error code
     */
    public native static int dbAdd(long context, String faceID, byte[] regTemplate);

    /**
     * Delete a ret-template from memory db
     * 从默认的1:N高速缓冲区删除一个人脸模板
     * @param context 算法实例指针
     * @param faceID  人脸ID
     * @return error code
     */
    public native static int dbDel(long context, String faceID);

    /**
     * Clear the memory db
     * 清空默认的1:N高速缓冲区
     * @param context 算法实例指针
     * @return error code
     */
    public native static int dbClear(long context);

    /**
     * Query the template count of memory db
     * 获取默认的1:N高速缓冲区的模板数
     * @param context 算法实例指针
     * @param count   返回的模板个数
     * @return error code
     */
    public native static int dbCount(long context, int[] count);

    /**
     * Identify
     * 1:N识别
     * @param context     算法实例指针
     * @param verTemplate 比对模板
     * @param faceIDs     返回人脸ID数组 "id0\tid1\tid2..."
     * @param score       返回比对分数
     * @param maxRetCount [in]:最大返回多少个
     *                    [out]:实际返回多少个
     * @param minScore    最低匹配分数。只有要识别的人脸与数据库中 的某一人脸模板的相似度达到该值时，才能识别 成功
     * @param maxScore    当要识别人脸与数据库中的某一人脸模板的相似 度达到该值时，识别成功立即返回
     * @return error code
     */
    public native static int dbIdentify(long context, byte[] verTemplate, byte[] faceIDs, int[] score, int[] maxRetCount, int minScore, int maxScore);

    /**
     * bayer 2 BGR24
     *
     * @param bayer   bayer data
     * @param width
     * @param height
     * @param imgData image raw data
     * @return
     */
    public native static int bayerToBGR24(byte[] bayer, int width, int height, byte[] imgData);
    public native static int loadImageFromMemoryExt(byte[] ImageFileData, int cbImageFileData, byte[] rawImage, int[] width, int[] height, int[] size);
    public native static int detectFacesExt(long context, byte[] rawImage, int width, int height, int[] detectedFaces,int colortype);
    public native static int setThumbnailParameter(long context,int thumbnailWidth, int thumbnailHeight, float thumbnailScale,float thumbnailQuality);

    /**
     * 在默认的1:N高速缓冲区中通过人脸ID号进行1:1比对
     * @param context     算法实例指针
     * @param faceID      人脸ID
     * @param verTemplate 比对的模板
     * @param score       返回比对分数
     * @return
     */
    public native static int dbVerifyByID(long context,String faceID,byte[] verTemplate,int[] score);
    public native static int dbInitExt(long context,long[] dbcontext);
    public native static int dbAddExt(long dbcontext,String faceID,byte[] regTemplate);
    public native static int dbDelExt(long dbcontext,String faceID);
    public native static int dbCountExt(long dbcontext,int[] count);
    public native static int dbIdentifyExt(long dbcontext, byte[] verTemplate, byte[] faceIDs, int[] score, int[] maxRetCount, int minScore, int maxScore);
    public native static int dbVerifyByIDExt(long dbcontext,String faceID,byte[] verTemplate,int[] score);
    public native static int dbFreeExt(long dbcontext);
    public native static int dbClearExt(long dbcontext);

}
