package com.thopham.projects.research.userservice.core

import io.grpc.stub.StreamObserver
import reactor.core.publisher.Mono

fun <T> Mono<T>.observe(observer: StreamObserver<T>){
    subscribe({item ->
        observer.onNext(item)
        observer.onCompleted()
    }, {err ->
        observer.onError(err)
    })
}