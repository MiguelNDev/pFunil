package br.com.pstore.pfunil.util;

import org.bukkit.ChatColor;

/**
 * Utilitário para formatação de mensagens
 */
public class MessageUtil {

    /**
     * Formata uma mensagem substituindo os códigos de cor
     * @param message Mensagem a ser formatada
     * @return Mensagem formatada
     */
    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
