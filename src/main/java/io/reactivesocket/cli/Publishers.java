/*
 * Copyright 2015 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.reactivesocket.cli;

import com.google.common.io.CharSource;
import io.reactivesocket.Payload;
import io.reactivesocket.util.PayloadImpl;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Publisher;

public final class Publishers {

    private Publishers() {
        // No instances.
    }

    /**
     * Return a publisher for consuming each line from the passed stream.
     *
     * @param inputStream to read.
     */
    public static Publisher<Payload> lines(CharSource inputStream) {
        return Flowable.just(inputStream)
                       .flatMapIterable(s -> s.readLines())
                       .onBackpressureBuffer()
                       .map(l -> (Payload) new PayloadImpl(l))
                       .subscribeOn(Schedulers.io());
    }
}
