package loginweibo;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pl on 15-7-19.
 */
public class weibo {
    public static void main(String[] args) throws NullPointerException{

        //登录页面的URL
        String Loginurl ="http://login.weibo.cn/login/";
        String firstpage = "http://weibo.cn/?vt=4";    //微博首页的网址
        String loginnum = "18829289882";
        String loginpwd = "pl19710504";

        CloseableHttpClient httpclient = HttpClients.createDefault();   //建立客户端的链接
        HttpGet httpget = new HttpGet(Loginurl);

        try{
            CloseableHttpResponse response = httpclient.execute(httpget);

            String responhtml = null;
            try {
                responhtml = EntityUtils.toString(response.getEntity());
                //System.out.println(responhtml);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

                 //将vk的值截出来,split将整个的html分为两个部分,截取下面的部分
                String vk = responhtml.split("<input type=\"hidden\" name=\"vk\" value=\"")[1].split("\" /><input")[0];
                System.out.println(vk);

                String pass = vk.split("_")[0];
                String finalpass = "password_"+pass;
                System.out.println(finalpass);
                response.close();

                List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
                pairs.add(new BasicNameValuePair("mobile",loginnum));
                pairs.add(new BasicNameValuePair(finalpass,"pl19710504"));
                pairs.add(new BasicNameValuePair("remember","on"));
                pairs.add(new BasicNameValuePair("vk",vk));

                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);   //返回的实体
                HttpPost httppost = new HttpPost(Loginurl);
                httppost.setEntity(entity);
                //检测以下响应状态是否正确
                CloseableHttpResponse  response2 = httpclient.execute(httppost);
                System.out.println(response2.getStatusLine().toString());
                httpclient.execute(httppost);  //登录操作
                System.out.println("success");

                HttpGet getinfo= new HttpGet("http://m.weibo.cn/p/100803?vt=4");
                CloseableHttpResponse res;
                res = httpclient.execute(getinfo);
                System.out.println("进入热门话题的页面:");
                //输出该页面的html文件
                System.out.println(EntityUtils.toString(res.getEntity()));
                res.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
