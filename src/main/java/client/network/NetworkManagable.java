package client.network;

import MessagesBase.ResponseEnvelope;
import org.springframework.util.MultiValueMap;

public interface NetworkManagable {
    public <T, S> ResponseEnvelope<S> postRequest(T body, String endpoint, MultiValueMap<String, String> queryParameters);
    public <S> ResponseEnvelope<S> getRequest(String endpoint, MultiValueMap<String, String> queryParameters);
}
