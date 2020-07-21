package com.gcy.bootwithutils.controller;

import com.gcy.bootwithutils.common.constants.Result;
import com.gcy.bootwithutils.common.dto.AppleAuthKey;
import com.gcy.bootwithutils.common.dto.AppleAuthKeys;
import com.gcy.bootwithutils.common.dto.ResolvedAppleTokenAlgInfo;
import com.gcy.bootwithutils.common.dto.ResolvedAppleTokenAuthInfo;
import com.gcy.bootwithutils.controller.base.BaseController;
import com.gcy.bootwithutils.httpclient.HttpResult;
import com.gcy.bootwithutils.service.json.JsonService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

/**
 * @ClassName AppleAuthorizationController
 * @Description 苹果账号登录验证
 * @Author Eddie
 * @Date 2020/07/16 09:43
 */

@RestController
@Slf4j
@RequestMapping("/appleAuth")
public class AppleAuthorizationController extends BaseController {



    /*
     *@Method AppleLogin
     *@Params identityToken APP获取的加密token
     * 测试 identityToken
     * eyJraWQiOiJBSURPUEsxIiwiYWxnIjoiUlMyNTYifQ.
     * eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLnNreW1pbmcuZGV2aWNlbW9uaXRvciIsImV4cCI6MTU2NTY2ODA4NiwiaWF0IjoxNTY1NjY3NDg2LCJzdWIiOiIwMDEyNDcuOTNiM2E3OTlhN2M4NGMwY2I0NmNkMDhmMTAwNzk3ZjIuMDcwNCIsImNfaGFzaCI6Ik9oMmFtOWVNTldWWTNkcTVKbUNsYmciLCJhdXRoX3RpbWUiOjE1NjU2Njc0ODZ9.
     * e-pdwK4iKWErr_Gcpkzo8JNi_MWh7OMnA15FvyOXQxTx0GsXzFT3qE3DmXqAar96nx3EqsHI1Qgquqt2ogyj-lLijK_46ifckdqPjncTEGzVWkNTX8uhY7M867B6aUnmR7u-cf2HsmhXrvgsJLGp2TzCI3oTp-kskBOeCPMyTxzNURuYe8zabBlUy6FDNIPeZwZXZqU0Fr3riv2k1NkGx5MqFdUq3z5mNfmWbIAuU64Z3yKhaqwGd2tey1Xxs4hHa786OeYFF3n7G5h-4kQ4lf163G6I5BU0etCRSYVKqjq-OL-8z8dHNqvTJtAYanB3OHNWCHevJFHJ2nWOTT3sbw
     *@Description Apple授权接口
     *@Author Eddie
     *@Date 2020/07/16 09:53
     */
    @ApiOperation("Apple三方登录")
    @PostMapping("/Login")
    @ResponseBody
    public Result AppleLogin(HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(request.getParameter("identityToken"))){
            return Result.failed("缺少必填项 identityToken");
        }
        String identityToken = request.getParameter("identityToken");
        HttpResult hr = httpclient.doGet("https://appleid.apple.com/auth/keys",null);
        AppleAuthKeys keysList;
        if (hr.getCode() == 200){
            keysList = JsonService.jsonToObject(hr.getBody(), AppleAuthKeys.class);
            if (keysList != null && !keysList.getKeys().isEmpty()){
                ResolvedAppleTokenAuthInfo token = verify(identityToken, keysList);
                if (token != null){
                    // TODO 此处接入业务代码
                    return Result.success(token);
                }else{
                    return Result.failed("解析token出错");
                }
            }
        }
        return Result.failed("获取Apple公钥失败");
    }

    /*
     *@Method verify
     *@Params [identityToken, keys]
     *@Description 遍历解析App段发来的identityToken
     *@Author Eddie
     *@Date 2020/07/16 14:41
     */
    public static ResolvedAppleTokenAuthInfo verify(String identityToken, AppleAuthKeys keys) throws Exception {
        if(keys == null){
            return null;
        }
        ResolvedAppleTokenAuthInfo res;
        //一般情况Apple会返回两组key 第一个不好用时候第二个好用 所以遍历进行验证
        for(AppleAuthKey key: keys.getKeys()){
            res = verifyKeyAndToken(identityToken, key);
            if(res != null){
                return res;
            }
        }
        return null;
    }

    /*
     *@Method verifyKeyAndToken
     *@Params [identityToken, publicKey]
     *@Description 公钥与token验证
     *@Author Eddie
     *@Date 2020/07/16 14:42
     */
    public static ResolvedAppleTokenAuthInfo verifyKeyAndToken(String identityToken, AppleAuthKey publicKey) throws Exception {
        String n = publicKey.getN();
        String e = publicKey.getE();

        //解析identityToken
        ResolvedAppleTokenAuthInfo resolvedAppleTokenAuthInfo = resolveIdentityToken(identityToken, publicKey.getKid());

        if(resolvedAppleTokenAuthInfo == null){
            return null;
        }

        //解析公钥
        PublicKey pubKey = getPublicKey(n,e);
        JwtParser jwtParser = Jwts.parser().setSigningKey(pubKey);
        jwtParser.requireIssuer("https://appleid.apple.com");
        //APP ID
        jwtParser.requireAudience(resolvedAppleTokenAuthInfo.getAud());
        //USER ID
        jwtParser.requireSubject(resolvedAppleTokenAuthInfo.getSub());

        try {
            //公钥验证
            Jws<Claims> claim = jwtParser.parseClaimsJws(identityToken);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                return resolvedAppleTokenAuthInfo;
            }
            return null;
        } catch (Exception e2) {
            //解析不成功会抛出异常 此处为了返回结果特殊处理
            //resolvedAppleTokenAuthInfo.setEmail("758418981@qq.com");
            //return resolvedAppleTokenAuthInfo;
            return null;
        }
    }

    /*
     *@Method resolveIdentityToken
     *@Params [identityToken]
     *@Description 解析APP端传来的token
     *@Author Eddie
     *@Date 2020/07/16 11:24
     */
    public static ResolvedAppleTokenAuthInfo resolveIdentityToken(String identityToken, String kid){
        String[] arr = identityToken.split("\\.");
        /* token格式为三段式，以[.]分割base64解码后数据结构如下
         * {"kid":"XXXXX","alg":"RS256"}
         * {"iss":"https://appleid.apple.com","aud":"com.XXXX.XXXX","exp":1565668086,"iat":1565667486,"sub":"xxxx.xxxxx.xxxxx","c_hash":"xxxxxxxx","auth_time":1565667486}
         * {�]��")a+��L��b�š��'^E�#�C��k�$Q��u�M=�o} 第三部分乱码
         */
        if(arr.length == 3){
            Base64 base64 = new Base64();
            String alg = new String(base64.decodeBase64(arr[0]));
            String auth = new String (base64.decodeBase64(arr[1]));
            String algStr = alg.substring(0, alg.indexOf("}")+1);
            String authStr = auth.substring(0, auth.indexOf("}")+1);
            ResolvedAppleTokenAlgInfo algInfo = JsonService.jsonToObject(algStr, ResolvedAppleTokenAlgInfo.class);
            System.out.println(algInfo.toString());
            ResolvedAppleTokenAuthInfo authInfo = JsonService.jsonToObject(authStr, ResolvedAppleTokenAuthInfo.class);
            System.out.println(authInfo.toString());
            if(authInfo != null && kid.equals(algInfo.getKid())){
                return authInfo;
            }
            System.out.println("apple login --------> resolveIdentityToken kid is " + algInfo.getKid()+ " mismatch with public key's kid " + kid + ", try using next key");
        }
        return null;
    }

    /*
     *@Method getPublicKey
     *@Params [modulus, publicExponent]
     *@Description 获得解密后的公钥
     *@Author Eddie
     *@Date 2020/07/16 11:11
     */
    public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Base64解码模数和公钥指数
        BigInteger bigIntModulus = new BigInteger(1, Base64.decodeBase64(modulus));
        BigInteger bigIntPrivateExponent = new BigInteger(1, Base64.decodeBase64(publicExponent));
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
}
