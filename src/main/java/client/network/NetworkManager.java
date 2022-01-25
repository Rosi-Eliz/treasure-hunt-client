package client.network;

import MessagesBase.ResponseEnvelope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class NetworkManager implements NetworkManagable {
    private String serverBaseUrl;
    private WebClient webClient;

    public NetworkManager(String serverBaseUrl) {
        this.serverBaseUrl = serverBaseUrl;
        webClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
				.build();
    }

    @Override
    public <T, S> ResponseEnvelope<S> postRequest(T body, String endpoint, MultiValueMap <String, String> queryParameters) {
        Mono<ResponseEnvelope> webAccess = webClient
                .method(HttpMethod.POST)
                .uri( builder -> builder
                        .path(endpoint)
                        .queryParams(queryParameters).build())
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.bodyToMono(ResponseEnvelope.class);

		ResponseEnvelope<S> result = webAccess.block();
		return result;
    }

    @Override
    public <S> ResponseEnvelope<S> getRequest(String endpoint, MultiValueMap<String, String> queryParameters) {
        Mono<ResponseEnvelope> webAccess = webClient
                .method(HttpMethod.GET)
                .uri( builder -> builder
                        .path(endpoint)
                        .queryParams(queryParameters).build())
                .retrieve()
                .bodyToMono(ResponseEnvelope.class);

        ResponseEnvelope<S> result = webAccess.block();
        return result;
    }
}
