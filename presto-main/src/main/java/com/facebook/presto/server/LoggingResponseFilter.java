/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.server;

import com.facebook.airlift.log.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import java.io.IOException;

@Logged
@Provider
public class LoggingResponseFilter
        implements ContainerResponseFilter
{
    private static final Logger log = Logger.get(LoggingResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException
    {
        log.info("RESPONS %s %08x: %s Body: %s",
                requestContext.getMethod(),
                requestContext.hashCode(),
                requestContext.getUriInfo().getRequestUri(),
                getEntityJSON(responseContext));
    }

    private String getEntityJSON(ContainerResponseContext responseContext) throws JsonProcessingException
    {
        try {
            if (responseContext.hasEntity()) {
                ObjectMapper mapper = new ObjectMapper();

                Object obj = responseContext.getEntity();

                return mapper.writeValueAsString(responseContext.getEntity());
            }
            else {
                return "none";
            }
        }
        catch (Exception e) {
            log.info("oops");
        }

        return "JSON error";
    }
}
