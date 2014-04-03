package grabber.utils;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;

/**
 * Created by nikita on 27.03.14.
 */
public class TwitterConfigurator {
    private static final String TOKEN_FILE = "token.dat";
    private static TwitterConfigurator instance;

    private Twitter twitter;

    public AccessToken getAccessToken() {
        return accessToken;
    }

    private AccessToken accessToken;

    public static TwitterConfigurator getInstance(){
        if(instance == null)
            instance = new TwitterConfigurator();
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

//    public ResponseList<Status> downloadFeed(TwitterFeed feed) throws TwitterException {
//        ResponseList<Status> userTimeline = twitter.getUserTimeline(feed.getAccountName(), new Paging(1, 50));
////        for (Status status : userTimeline) {
////            System.out.println(status.getUser()+" "+status.getText());
////        }
//        return userTimeline;
//
//    }

    public Twitter getTwitter(){
        return twitter;
    }

    public void loadAccessTokenInteractive(){
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
        cb.setIncludeEntitiesEnabled(true);
        cb.setIncludeEntitiesEnabled(true);
        cb.setIncludeMyRetweetEnabled(true);
        Configuration build = cb.build();
        TwitterFactory twitterFactory = new TwitterFactory(build);
        twitter = twitterFactory.getInstance();
        accessToken = readAccessToken();

        if(accessToken != null)
            twitter.setOAuthAccessToken(accessToken);

    }
}
