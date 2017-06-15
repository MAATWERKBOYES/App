package api;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

/**
 * Created by Jeroe on 15-6-2017.
 */

public class ApiErrorHandler extends DefaultResponseErrorHandler
{
    @Override
    public void handleError(ClientHttpResponse response) throws IOException
    {

    }
}
