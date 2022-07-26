package org.fatsnake.fatrpc.framework.core.filter.server;


import org.fatsnake.fatrpc.framework.core.common.RpcInvocation;
import org.fatsnake.fatrpc.framework.core.filter.IServerFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author linhao
 * @Date created in 8:40 下午 2022/1/29
 */
public class ServerBeforeFilterChain {

    private static List<IServerFilter> iServerFilters = new ArrayList<>();

    public void addServerFilter(IServerFilter iServerFilter) {
        iServerFilters.add(iServerFilter);
    }

    public void doFilter(RpcInvocation rpcInvocation) {
        for (IServerFilter iServerFilter : iServerFilters) {
            iServerFilter.doFilter(rpcInvocation);
        }
    }
}
