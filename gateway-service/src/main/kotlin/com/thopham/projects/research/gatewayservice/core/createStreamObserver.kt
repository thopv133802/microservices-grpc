package com.thopham.projects.research.gatewayservice.core

import io.grpc.stub.StreamObserver
import reactor.core.publisher.MonoProcessor


fun <T> MonoProcessor<T>.createStreamObserver(): StreamObserver<T> {
    return object: StreamObserver<T> {
        override fun onNext(value: T) {
            this@createStreamObserver.onNext(value)
        }

        override fun onError(t: Throwable) {
            this@createStreamObserver.onError(t)
        }

        override fun onCompleted() {
            this@createStreamObserver.onComplete()
        }
    }
}