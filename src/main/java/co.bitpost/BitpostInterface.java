package co.bitpost;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BitpostInterface {

    private String baseURL;
    String wallettoken;
    String apiKey;


    public BitpostInterface(){
        this(false, null, null);
    }

    public BitpostInterface(boolean isTestnet){
        this(isTestnet, null, null);
    }

    public BitpostInterface(boolean isTestnet, @Nullable String wallettolen, String apiKey){
        if(isTestnet){
            baseURL = "https://testnet-api.bitpost.co";
        }else{
            baseURL = "https://api.bitpost.co";
        }
        this.wallettoken = wallettolen;
        this.apiKey = apiKey;
    }

    public BitpostRequest createBitpostRequest(List<String> rawTxs, long targetInSeconds){
        return createBitpostRequest(rawTxs, targetInSeconds, 1L, false);
    }

    public BitpostRequest createBitpostRequest(List<String> rawTxs, long targetInSeconds, @Nullable Long delay, boolean broadcastLowestFeerate){
        return new BitpostRequest(rawTxs, targetInSeconds, baseURL, wallettoken, apiKey, delay, broadcastLowestFeerate);
    }

    public List<Double> getFeerates(double maxFeerate, int size) throws Exception{
        return this.getFeerates(maxFeerate, size, null, true);
    }

    public List<Double> getFeerates(double maxFeerate, int size, @Nullable Long target, boolean canReduceFee) throws Exception{
         GetRequest feeRequest = Unirest.get(this.baseURL + "/feerateset")
                 .queryString("maxfeerate", maxFeerate)
                 .queryString("size", size)
                 .queryString("canreducefee", canReduceFee);
         if(target!=null) feeRequest.queryString("target", target);

         HttpResponse<JsonNode> response = feeRequest.asJson();
         if(response.getStatus() >= 400) throw new Exception("Failed to get feerates");

         JSONArray feeJsonArray = response.getBody().getObject().getJSONObject("data").getJSONArray("feerates");
        return IntStream.range(0, feeJsonArray.length()).mapToObj(feeJsonArray::getDouble).collect(Collectors.toList());
    }
}


