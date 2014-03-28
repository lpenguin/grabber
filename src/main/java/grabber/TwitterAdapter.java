package grabber;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterAdapter {
    private static final String TOKEN_FILE = "token.dat";
    private static TwitterAdapter instance;

    private Twitter twitter;

    public AccessToken getAccessToken() {
        return accessToken;
    }

    private AccessToken accessToken;

    public static TwitterAdapter getInstance(){
        if(instance == null)
            instance = new TwitterAdapter();
        return instance;
    }

    private void writeAccessToken(AccessToken accessToken){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(TOKEN_FILE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(accessToken);
            fileOutputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private AccessToken readAccessToken(){
        File file = new File(TOKEN_FILE);
        AccessToken accessToken = null;
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(TOKEN_FILE);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                accessToken = (AccessToken)objectInputStream.readObject();
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return accessToken;
    }

    private void loadAccessTokenInteractive(){
        RequestToken requestToken = null;
        try {
            requestToken = twitter.getOAuthRequestToken();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        accessToken = null;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            System.out.println(requestToken.getAuthorizationURL());
            System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
            String pin = null;
            try {
                pin = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                if(pin.length() > 0){
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }else{
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if(401 == te.getStatusCode()){
                    System.out.println("Unable to get the access token.");
                }else{
                    te.printStackTrace();
                }
            }
        }

        writeAccessToken(accessToken);
    }

    public void configure() throws IOException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey("oywTVGhMSHULqO6h3JzA");
        cb.setOAuthConsumerSecret("K1qj6qRTzjvNj4OEsyovQNxVULSGweyp25RcZvAdU");
        cb.setDebugEnabled(true);
        TwitterFactory twitterFactory = new TwitterFactory(cb.build());

        twitter = twitterFactory.getInstance();

        accessToken = readAccessToken();
    }
}
