package com.kashmirinfo.yesstudyinmalaysia;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {
    public static final String BASE = "https://yesstudyinmalaysia.kashmirinfo.com/";
    private final Context context;
    private final SharedPreferences prefs;
    private final ExecutorService executor=Executors.newCachedThreadPool();
    public interface Callback { void done(JSONObject json,int code,Exception error); }
    public ApiClient(Context c){context=c.getApplicationContext();prefs=context.getSharedPreferences("yesstudy_native",Context.MODE_PRIVATE);}
    public void mobileGet(String action,Callback cb){request("api/mobile/index.php?action="+enc(action),"GET",null,"application/json",cb);}
    public void mobilePost(String action,JSONObject body,Callback cb){request("api/mobile/index.php?action="+enc(action),"POST",body==null?"{}":body.toString(),"application/json; charset=utf-8",cb);}
    public void publicGet(String relative,Callback cb){request(relative,"GET",null,"application/json",cb);}
    public void postForm(String relative,Map<String,String> fields,Callback cb){
        StringBuilder b=new StringBuilder();
        try{for(Map.Entry<String,String> e:fields.entrySet()){if(b.length()>0)b.append('&');b.append(URLEncoder.encode(e.getKey(),"UTF-8")).append('=').append(URLEncoder.encode(e.getValue()==null?"":e.getValue(),"UTF-8"));}}catch(Exception ignored){}
        request(relative,"POST",b.toString(),"application/x-www-form-urlencoded; charset=utf-8",cb);
    }
    private void request(String relative,String method,String body,String contentType,Callback cb){
        executor.execute(()->{
            HttpURLConnection c=null;int code=-1;JSONObject out=null;Exception err=null;
            try{
                c=(HttpURLConnection)new URL(BASE+relative).openConnection();c.setConnectTimeout(15000);c.setReadTimeout(20000);c.setRequestMethod(method);c.setRequestProperty("Accept","application/json");c.setRequestProperty("User-Agent","YesStudyNative/2.0 Android");
                String cookie=prefs.getString("cookie","");if(!cookie.isEmpty())c.setRequestProperty("Cookie",cookie);
                if(body!=null){c.setDoOutput(true);c.setRequestProperty("Content-Type",contentType);try(OutputStream os=c.getOutputStream()){os.write(body.getBytes(StandardCharsets.UTF_8));}}
                code=c.getResponseCode();captureCookie(c);
                InputStream in=(code>=200&&code<400)?c.getInputStream():c.getErrorStream();String text=read(in);try{out=new JSONObject(text);}catch(Exception parse){out=new JSONObject();out.put("ok",false);out.put("message",text.isEmpty()?"Unexpected server response.":text);}
            }catch(Exception e){err=e;try{out=new JSONObject().put("ok",false).put("message","Network error: "+e.getMessage());}catch(Exception ignored){}}
            finally{if(c!=null)c.disconnect();}
            cb.done(out,code,err);
        });
    }
    private void captureCookie(HttpURLConnection c){String set=c.getHeaderField("Set-Cookie");if(set==null)return;String first=set.split(";",2)[0].trim();if(first.toLowerCase().startsWith("phpsessid="))prefs.edit().putString("cookie",first).apply();}
    private String read(InputStream in)throws Exception{if(in==null)return "";BufferedReader r=new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8));StringBuilder b=new StringBuilder();String line;while((line=r.readLine())!=null)b.append(line);r.close();return b.toString();}
    private static String enc(String s){try{return URLEncoder.encode(s,"UTF-8");}catch(Exception e){return s;}}
    public void clearSession(){prefs.edit().remove("cookie").apply();}
    public String sessionCookie(){return prefs.getString("cookie","");}
    public String displayName(Uri uri){
        String result=null;Cursor cur=context.getContentResolver().query(uri,null,null,null,null);if(cur!=null){try{if(cur.moveToFirst()){int i=cur.getColumnIndex(OpenableColumns.DISPLAY_NAME);if(i>=0)result=cur.getString(i);}}finally{cur.close();}}
        if(result==null)result=uri.getLastPathSegment();return result==null?"document":result;
    }
    public String mime(Uri uri){String m=context.getContentResolver().getType(uri);if(m!=null)return m;String ext=MimeTypeMap.getFileExtensionFromUrl(uri.toString());String guess=MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);return guess==null?"application/octet-stream":guess;}
    public void uploadDocument(int documentId,Uri uri,Callback cb){
        executor.execute(()->{
            HttpURLConnection c=null;JSONObject out=null;int code=-1;Exception err=null;String boundary="----YesStudy"+System.currentTimeMillis();
            try{
                c=(HttpURLConnection)new URL(BASE+"api/mobile/index.php?action=upload_document").openConnection();c.setConnectTimeout(20000);c.setReadTimeout(60000);c.setRequestMethod("POST");c.setDoOutput(true);c.setRequestProperty("Accept","application/json");c.setRequestProperty("Content-Type","multipart/form-data; boundary="+boundary);String cookie=sessionCookie();if(!cookie.isEmpty())c.setRequestProperty("Cookie",cookie);
                try(OutputStream os=c.getOutputStream()){
                    write(os,"--"+boundary+"\r\nContent-Disposition: form-data; name=\"document_id\"\r\n\r\n"+documentId+"\r\n");
                    String name=displayName(uri),mime=mime(uri);write(os,"--"+boundary+"\r\nContent-Disposition: form-data; name=\"document\"; filename=\""+safe(name)+"\"\r\nContent-Type: "+mime+"\r\n\r\n");
                    try(InputStream in=new BufferedInputStream(context.getContentResolver().openInputStream(uri))){byte[] buf=new byte[8192];int n;while((n=in.read(buf))>0)os.write(buf,0,n);}write(os,"\r\n--"+boundary+"--\r\n");
                }
                code=c.getResponseCode();captureCookie(c);InputStream in=(code>=200&&code<400)?c.getInputStream():c.getErrorStream();String txt=read(in);out=new JSONObject(txt);
            }catch(Exception e){err=e;try{out=new JSONObject().put("ok",false).put("message","Upload failed: "+e.getMessage());}catch(Exception ignored){}}
            finally{if(c!=null)c.disconnect();}
            cb.done(out,code,err);
        });
    }
    private static void write(OutputStream os,String s)throws Exception{os.write(s.getBytes(StandardCharsets.UTF_8));}
    private static String safe(String s){return s.replace("\"","").replace("\r","").replace("\n","");}
    public long download(String relative,String title,String mime,boolean withSession){
        DownloadManager.Request r=new DownloadManager.Request(Uri.parse(BASE+relative));r.setTitle(title==null?"Download":title);r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);if(mime!=null&&!mime.isEmpty())r.setMimeType(mime);if(withSession&&!sessionCookie().isEmpty())r.addRequestHeader("Cookie",sessionCookie());r.addRequestHeader("User-Agent","YesStudyNative/2.0 Android");String file=(title==null||title.isEmpty())?"yesstudy-download":title.replaceAll("[^a-zA-Z0-9._-]","_");r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);DownloadManager dm=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);return dm.enqueue(r);
    }
}