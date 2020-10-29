package cn.eby.book.common.constant;

/**
 * @Auther: 徐长乐
 * @Date: 2020/09/14/9:56
 * @Description:  支付宝批请求配置
 */
public class AlipayConcig {

//    商户appid
    public static final String APP_ID = "2021001193683695";

//    商户私钥
    public static final String RSA2_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCKxNLaKvHQMI+HKy6vkvgUTpcH4kH97xlWNWFLtJKXvFhk34gAvhxl5/I9nMFBscBubp2aRf7pMprFP5r8bKqc29BZkWtk0hJuSM11X9WCrXnZx+olZ5MsLc7ErJv7+LckLa+vbv6Bsc6i+FjOU8mA3b5nwml6ugSYydn8Ndqo4o+ymzwEZcrZNTbK7Cofx/BvUhKgESyIMkdX0fHWQUbyY8s8VOX6LV5t1gmwlmO56K82cCoEllVcxm7lFt0cuZX8C6trJzbB1llGERb5+UmS0PV/Eg1m+J9V2g4JJRel7RXL395KxTgsR/VcPI9LzAERieHVQDjq/iNurYHSQvRhAgMBAAECggEAA24iGCp/l7nbkmfK26QyZa4bpaI+J1zFr4/aO9jW1tFdDgFIh1wrr4kwP72kOU9/hjtNwgaA6prJoJ6V4i99eCsm10/066UVt4wtJ/2adYR8Qpnie9I3nRv0xH/Py0a2oDpQ4XFI8UPH69dfupnvnTPeBQqVOOdoPa6fqNBb+1i1AoVTgZUipoRW9LqRpGzh6EnkzRnDQj2wX432BgXKIFRi72r1igKjtThqDM75wpMbZqZ33y9379zJKFudDaU1EvV65TXFH6Sr1RXsiKZMAAHm5XGtq71ATSKc3tN+/v0t0sRIFyDaGUguHqy5lnZmjd2v75jTfOR/NrhG4yoHwQKBgQDNPA+fQdTtbCJS6coqalLcG7+BryRyrIkkM6ECdQ2nt5gtyNsfpwKR4dtVWH4a+fRt3gDA/tyfCAfpoO/NMvoe2nyqY19z09lrDow67Qo+2nMPJ+CDGqQ1IcZlqDGrCs+i5O0jEW1l5R+w50hqDOQaYCifeeO9yGCZnFtFPsVhnQKBgQCtF/5VgVu7WQGXOgSMMZTyo+FoA3uGBn5foFiCB3khG2zqmRotNCqHCb9nCOuhoXnysABk9+xrN42wEXZ7zZQNi1j7jGp5YgKzyUGZ59MgesCuL+me7vMQs0zcnZMOqaAqOCgCHL/sfShcqFYUTrXN/7jWEKc2mNH8Wu9gD950lQKBgHR4SUVPzs6ON0xmcNxxSmSvOpcWFdEeKRXqVs1WMFGl1y7kdbYYyVSQoDBm7+nPP++5tSPRftKhdBwZwPzqmRc5VtY7XsgUyaX/s4UdUFsrDXDkaHz+eYHNkRHPyaAPmpYO5KSqFzNkO//gwS+t8SUmvOcD/UKOnIOQQBuj68/hAoGAX/dwhBmPMUS8tApvoQaQTSJ9Az0DDCCnif8NczIPf0KEVWEC+VL1zR6jZs20Tk2HnfzkixZFloAbZyWXajZlQkki0XxdP+UOGH2ZiGnWTSihkTFxNjPGwViXYa1fbf6vlRjDXcBwy/3sFcsixmmJNKf144EiJAwASAhmtPy3Wi0CgYAWlYV9F982acdjniMKFxCm5Hh5a8ZBzF5Ze+4rF99Zkq29057XD+U7j/G9uXoBLJ1re7rxAmwG6pfS5iza7so0qgoprsNkd895IsvtfLMciMp4OZkbwa41y0P4uEAWLPeNM+jS0AYDNEKCQaKczNCD3jpX17WSU2WQRhbYXvE5Vg==";

//    支付宝公公钥
    public static final String ALIPAY_RSA2_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl0D9ro/dJPTSEmOVpmc3ZyeMhpY9Cw6bHfPWU8UVwUGM82HnEqDOizVlDthjpk/P8Nu20WNBuK3mg1aKwqsA0qtlAOhjLXj5dHhCQh+OEBfMWBG3Ubs1EI43upS9CWyV8IY1YdFQLdsIfGvCO1gKxsvf+eGMnjiNbKeKeYm51UZ0vr18PDckE4xMaWQx4Ek6bmZcAJYcCsjLI/V89gfi50b3zQzy0KGC7kcSXI/oAfo6RpxLvzR/OGoMjLm6I0O0e+LLsr87LClQ3YT+dEvFBeRMTSHdLi8lSiZKbrcaFQLDXWLfk6TcYm97mAoWeg9N3whdWXt/o2bku+m2/GIjOwIDAQAB";

//    服务器异步回调地址
    public static final String NOTIFY_URL = "";

//    服务器同步回跳地址
    public static final String RETURN_URL = "";

//    支付宝网关
    public static final String ALIPAY_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

//    编码
    public static final String CHARSET = "UTF-8";

//    返回格式
    public static final String FORMAT = "json";

//    签名方式
    public static final String SIGN_TYPE = "RSA2";

}
