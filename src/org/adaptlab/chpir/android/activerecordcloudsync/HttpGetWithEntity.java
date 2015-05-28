package org.adaptlab.chpir.android.activerecordcloudsync;

import java.net.URI;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;

public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    
	public HttpGetWithEntity() {
        super();
    }

    public HttpGetWithEntity(URI uri) {
        super();
        setURI(uri);
    }

    public HttpGetWithEntity(String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return HttpGet.METHOD_NAME;
    }
}
