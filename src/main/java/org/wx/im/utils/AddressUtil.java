
package org.wx.im.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 根据ip查询地址
 *
 * @author WangXin
 */
@Slf4j
public final class AddressUtil {

    private AddressUtil() {
    }

    /**
     * 获取当前主机ip地址
     *
     * @return
     * @throws SocketException
     */
    public static String getHostIp() throws SocketException {
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress ip = addresses.nextElement();
                if (ip instanceof Inet4Address
                        // loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        && !ip.isLoopbackAddress()
                        && !ip.getHostAddress().contains(StrPool.COLON)) {
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }

}
