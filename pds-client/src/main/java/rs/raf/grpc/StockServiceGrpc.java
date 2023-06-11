package rs.raf.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: rpcApi.proto")
public final class StockServiceGrpc {

  private StockServiceGrpc() {}

  public static final String SERVICE_NAME = "StockService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      rs.raf.grpc.RpcApi.StockDto> getGetAllStocksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getAllStocks",
      requestType = com.google.protobuf.Empty.class,
      responseType = rs.raf.grpc.RpcApi.StockDto.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      rs.raf.grpc.RpcApi.StockDto> getGetAllStocksMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, rs.raf.grpc.RpcApi.StockDto> getGetAllStocksMethod;
    if ((getGetAllStocksMethod = StockServiceGrpc.getGetAllStocksMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getGetAllStocksMethod = StockServiceGrpc.getGetAllStocksMethod) == null) {
          StockServiceGrpc.getGetAllStocksMethod = getGetAllStocksMethod = 
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, rs.raf.grpc.RpcApi.StockDto>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "StockService", "getAllStocks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.StockDto.getDefaultInstance()))
                  .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("getAllStocks"))
                  .build();
          }
        }
     }
     return getGetAllStocksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.BidRequest,
      rs.raf.grpc.RpcApi.BidResponse> getBidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "bid",
      requestType = rs.raf.grpc.RpcApi.BidRequest.class,
      responseType = rs.raf.grpc.RpcApi.BidResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.BidRequest,
      rs.raf.grpc.RpcApi.BidResponse> getBidMethod() {
    io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.BidRequest, rs.raf.grpc.RpcApi.BidResponse> getBidMethod;
    if ((getBidMethod = StockServiceGrpc.getBidMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getBidMethod = StockServiceGrpc.getBidMethod) == null) {
          StockServiceGrpc.getBidMethod = getBidMethod = 
              io.grpc.MethodDescriptor.<rs.raf.grpc.RpcApi.BidRequest, rs.raf.grpc.RpcApi.BidResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "StockService", "bid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.BidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.BidResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("bid"))
                  .build();
          }
        }
     }
     return getBidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.AskRequest,
      rs.raf.grpc.RpcApi.AskResponse> getAskMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ask",
      requestType = rs.raf.grpc.RpcApi.AskRequest.class,
      responseType = rs.raf.grpc.RpcApi.AskResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.AskRequest,
      rs.raf.grpc.RpcApi.AskResponse> getAskMethod() {
    io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.AskRequest, rs.raf.grpc.RpcApi.AskResponse> getAskMethod;
    if ((getAskMethod = StockServiceGrpc.getAskMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getAskMethod = StockServiceGrpc.getAskMethod) == null) {
          StockServiceGrpc.getAskMethod = getAskMethod = 
              io.grpc.MethodDescriptor.<rs.raf.grpc.RpcApi.AskRequest, rs.raf.grpc.RpcApi.AskResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "StockService", "ask"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.AskRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.AskResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("ask"))
                  .build();
          }
        }
     }
     return getAskMethod;
  }

  private static volatile io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.OrderDto,
      com.google.protobuf.Empty> getOrderMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "order",
      requestType = rs.raf.grpc.RpcApi.OrderDto.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.OrderDto,
      com.google.protobuf.Empty> getOrderMethod() {
    io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.OrderDto, com.google.protobuf.Empty> getOrderMethod;
    if ((getOrderMethod = StockServiceGrpc.getOrderMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getOrderMethod = StockServiceGrpc.getOrderMethod) == null) {
          StockServiceGrpc.getOrderMethod = getOrderMethod = 
              io.grpc.MethodDescriptor.<rs.raf.grpc.RpcApi.OrderDto, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "StockService", "order"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.OrderDto.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("order"))
                  .build();
          }
        }
     }
     return getOrderMethod;
  }

  private static volatile io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.TransactionsRequest,
      rs.raf.grpc.RpcApi.TransactionsReply> getTransactionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "transactions",
      requestType = rs.raf.grpc.RpcApi.TransactionsRequest.class,
      responseType = rs.raf.grpc.RpcApi.TransactionsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.TransactionsRequest,
      rs.raf.grpc.RpcApi.TransactionsReply> getTransactionsMethod() {
    io.grpc.MethodDescriptor<rs.raf.grpc.RpcApi.TransactionsRequest, rs.raf.grpc.RpcApi.TransactionsReply> getTransactionsMethod;
    if ((getTransactionsMethod = StockServiceGrpc.getTransactionsMethod) == null) {
      synchronized (StockServiceGrpc.class) {
        if ((getTransactionsMethod = StockServiceGrpc.getTransactionsMethod) == null) {
          StockServiceGrpc.getTransactionsMethod = getTransactionsMethod = 
              io.grpc.MethodDescriptor.<rs.raf.grpc.RpcApi.TransactionsRequest, rs.raf.grpc.RpcApi.TransactionsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "StockService", "transactions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.TransactionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  rs.raf.grpc.RpcApi.TransactionsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new StockServiceMethodDescriptorSupplier("transactions"))
                  .build();
          }
        }
     }
     return getTransactionsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StockServiceStub newStub(io.grpc.Channel channel) {
    return new StockServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StockServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new StockServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StockServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new StockServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class StockServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAllStocks(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.StockDto> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAllStocksMethod(), responseObserver);
    }

    /**
     */
    public void bid(rs.raf.grpc.RpcApi.BidRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.BidResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBidMethod(), responseObserver);
    }

    /**
     */
    public void ask(rs.raf.grpc.RpcApi.AskRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.AskResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAskMethod(), responseObserver);
    }

    /**
     */
    public void order(rs.raf.grpc.RpcApi.OrderDto request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getOrderMethod(), responseObserver);
    }

    /**
     */
    public void transactions(rs.raf.grpc.RpcApi.TransactionsRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.TransactionsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getTransactionsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAllStocksMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.google.protobuf.Empty,
                rs.raf.grpc.RpcApi.StockDto>(
                  this, METHODID_GET_ALL_STOCKS)))
          .addMethod(
            getBidMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                rs.raf.grpc.RpcApi.BidRequest,
                rs.raf.grpc.RpcApi.BidResponse>(
                  this, METHODID_BID)))
          .addMethod(
            getAskMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                rs.raf.grpc.RpcApi.AskRequest,
                rs.raf.grpc.RpcApi.AskResponse>(
                  this, METHODID_ASK)))
          .addMethod(
            getOrderMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                rs.raf.grpc.RpcApi.OrderDto,
                com.google.protobuf.Empty>(
                  this, METHODID_ORDER)))
          .addMethod(
            getTransactionsMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                rs.raf.grpc.RpcApi.TransactionsRequest,
                rs.raf.grpc.RpcApi.TransactionsReply>(
                  this, METHODID_TRANSACTIONS)))
          .build();
    }
  }

  /**
   */
  public static final class StockServiceStub extends io.grpc.stub.AbstractStub<StockServiceStub> {
    private StockServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StockServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StockServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAllStocks(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.StockDto> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetAllStocksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bid(rs.raf.grpc.RpcApi.BidRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.BidResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getBidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ask(rs.raf.grpc.RpcApi.AskRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.AskResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getAskMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void order(rs.raf.grpc.RpcApi.OrderDto request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOrderMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void transactions(rs.raf.grpc.RpcApi.TransactionsRequest request,
        io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.TransactionsReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getTransactionsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StockServiceBlockingStub extends io.grpc.stub.AbstractStub<StockServiceBlockingStub> {
    private StockServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StockServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StockServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<rs.raf.grpc.RpcApi.StockDto> getAllStocks(
        com.google.protobuf.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), getGetAllStocksMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<rs.raf.grpc.RpcApi.BidResponse> bid(
        rs.raf.grpc.RpcApi.BidRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getBidMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<rs.raf.grpc.RpcApi.AskResponse> ask(
        rs.raf.grpc.RpcApi.AskRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getAskMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty order(rs.raf.grpc.RpcApi.OrderDto request) {
      return blockingUnaryCall(
          getChannel(), getOrderMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<rs.raf.grpc.RpcApi.TransactionsReply> transactions(
        rs.raf.grpc.RpcApi.TransactionsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getTransactionsMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StockServiceFutureStub extends io.grpc.stub.AbstractStub<StockServiceFutureStub> {
    private StockServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private StockServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StockServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new StockServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> order(
        rs.raf.grpc.RpcApi.OrderDto request) {
      return futureUnaryCall(
          getChannel().newCall(getOrderMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ALL_STOCKS = 0;
  private static final int METHODID_BID = 1;
  private static final int METHODID_ASK = 2;
  private static final int METHODID_ORDER = 3;
  private static final int METHODID_TRANSACTIONS = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StockServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StockServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ALL_STOCKS:
          serviceImpl.getAllStocks((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.StockDto>) responseObserver);
          break;
        case METHODID_BID:
          serviceImpl.bid((rs.raf.grpc.RpcApi.BidRequest) request,
              (io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.BidResponse>) responseObserver);
          break;
        case METHODID_ASK:
          serviceImpl.ask((rs.raf.grpc.RpcApi.AskRequest) request,
              (io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.AskResponse>) responseObserver);
          break;
        case METHODID_ORDER:
          serviceImpl.order((rs.raf.grpc.RpcApi.OrderDto) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_TRANSACTIONS:
          serviceImpl.transactions((rs.raf.grpc.RpcApi.TransactionsRequest) request,
              (io.grpc.stub.StreamObserver<rs.raf.grpc.RpcApi.TransactionsReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StockServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return rs.raf.grpc.RpcApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StockService");
    }
  }

  private static final class StockServiceFileDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier {
    StockServiceFileDescriptorSupplier() {}
  }

  private static final class StockServiceMethodDescriptorSupplier
      extends StockServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StockServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StockServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StockServiceFileDescriptorSupplier())
              .addMethod(getGetAllStocksMethod())
              .addMethod(getBidMethod())
              .addMethod(getAskMethod())
              .addMethod(getOrderMethod())
              .addMethod(getTransactionsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
