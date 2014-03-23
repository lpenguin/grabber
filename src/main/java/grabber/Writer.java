package grabber;

import java.net.URL;

/**
 * Created by nikita on 23.03.14.
 */
public class Writer {
    public static class Result{
        public URL getUrl() {
            return url;
        }

        public String getBody() {
            return body;
        }

        public Result(URL url, String body) {

            this.url = url;
            this.body = body;
        }

        private URL url;
        private String body;


    }

    public void write(Result result){}
}
