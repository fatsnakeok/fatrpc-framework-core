package org.fatsnake.fatrpc.framework.core.common.exception;


import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;

/**
 * @Author linhao
 * @Date created in 9:53 下午 2022/3/5
 */
public class MaxServiceLimitRequestException extends FatRpcException {

    public MaxServiceLimitRequestException(RpcInvocation rpcInvocation) {
        super(rpcInvocation);
    }
}
