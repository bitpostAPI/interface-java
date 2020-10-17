package co.bitpost;

import kong.unirest.*;
import kong.unirest.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.List;


public class BitpostRequest {

    String apiKey;
    String wallettoken;
    String baseURL;

    List<String> rawTxs;
    long absolute_epoch_target;
    long delay;
    boolean broadcast_lowest_feerate;

    String id;
    JSONObject answer;

    static Logger LOGGER = LoggerFactory.getLogger(BitpostRequest.class);

    public BitpostRequest(List<String> rawTxs, long targetInSeconds, String baseURL, @Nullable String wallettoken,
                           @Nullable String apiKey){
        this(rawTxs, targetInSeconds, baseURL, wallettoken, apiKey, 1L, false);
    }

    public BitpostRequest(List<String> rawTxs, long targetInSeconds, String baseURL, @Nullable String wallettoken,
                          @Nullable String apiKey, @Nullable Long delay, boolean broadcastLowestFeerate){
        this.rawTxs = rawTxs;
        this.absolute_epoch_target = toEpoch(targetInSeconds);
        if(delay==null) {
            this.delay = 1;
        } else if(delay > 1){
            this.delay = toEpoch(delay);
        } else{
            this.delay = delay;
        }
        this.broadcast_lowest_feerate = broadcastLowestFeerate;
        this.baseURL = baseURL;
        this.wallettoken = wallettoken;
        this.apiKey = apiKey;
    }

    static long toEpoch(long rawTarget){
        if(rawTarget < 100_000_000){
            return Math.round(rawTarget + Instant.now().getEpochSecond());
        } else if(rawTarget > 10_000_000_000L){
            return Math.round(rawTarget/1000.0);
        } else{
            return rawTarget;
        }
    }

    public JSONObject sendRequest(){
        RequestBodyEntity postRequest = Unirest.post(this.baseURL + "/request")
                .body(rawTxs);

        setQueryString(postRequest);
        LOGGER.info("Sending " + rawTxs.size() + " signed transactions...");
        LOGGER.info("URL=" + postRequest.getUrl());

        HttpResponse<JsonNode> response = postRequest.asJson();
        this.answer = response.getBody().getObject();

        if(response.getStatus() < 400) this.id = answer.getJSONObject("data").getString("id");

        LOGGER.info("Status code=" + response.getStatus());
        LOGGER.info(answer.toString());
        return answer;
    }

    private void setQueryString(RequestBodyEntity postRequest) {
        postRequest.queryString("target", absolute_epoch_target);
        if(broadcast_lowest_feerate) postRequest.queryString("broadcast", 0);
        if(this.wallettoken != null) postRequest.queryString("wallettoken", wallettoken);
        if(this.apiKey != null) postRequest.queryString("key", this.apiKey);
    }

}
