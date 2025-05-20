package br.com.pstore.pfunil.gui;

import br.com.pstore.pfunil.PFunil;
import br.com.pstore.pfunil.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface simplificada para gerenciar limites de funis
 * Compatível com Minecraft 1.8.8
 */
public class AdminGUI {

    private static final String GUI_TITLE = "§8PFunil - Gerenciamento";
    private static final int GUI_SIZE = 9; // 1 linha x 9 colunas
    
    /**
     * Abre a GUI de administração para o jogador
     * @param player Jogador que verá a GUI
     * @param plugin Instância do plugin
     */
    public static void openAdminGUI(Player player, PFunil plugin) {
        Inventory inventory = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);
        
        // Informações do plugin
        ItemStack infoItem = createItem(Material.HOPPER, 
                "§6§lPFunil", 
                "§7Plugin de gerenciamento de funis",
                "§7Versão: §f" + plugin.getDescription().getVersion(),
                "§7Autor: §fpStore",
                "",
                "§eClique para recarregar a configuração");
        inventory.setItem(0, infoItem);
        
        // Lista de jogadores
        ItemStack playerListItem = createItem(Material.CHEST, 
                "§a§lLista de Jogadores", 
                "§7Clique para ver todos os jogadores");
        inventory.setItem(2, playerListItem);
        
        // Limite padrão
        int defaultLimit = plugin.getConfigManager().getDefaultHopperLimit();
        ItemStack defaultLimitItem = createItem(Material.STONE, 
                "§c§lLimite Padrão", 
                "§7Limite atual: §f" + defaultLimit);
        inventory.setItem(4, defaultLimitItem);
        
        // Adicionar/Remover limite para jogador
        ItemStack limitItem = createItem(Material.PAPER, 
                "§e§lDefinir Limite", 
                "§7Define limite para um jogador específico",
                "§7Uso: /pfunil definir <jogador> <limite>");
        inventory.setItem(6, limitItem);
        
        // Verifica limite de um jogador
        ItemStack checkItem = createItem(Material.COMPASS, 
                "§b§lVerificar Jogador", 
                "§7Verifica os limites de um jogador",
                "§7Uso: /pfunil info <jogador>");
        inventory.setItem(8, checkItem);
        
        // Abre a GUI para o jogador
        player.openInventory(inventory);
    }
    
    /**
     * Cria um item para a GUI
     * @param material Material do item
     * @param name Nome do item
     * @param lore Descrição do item (cada String é uma linha)
     * @return Item criado
     */
    private static ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(name);
        
        if (lore.length > 0) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            meta.setLore(loreList);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    /**
     * Abre a GUI de configuração de limite para um jogador específico
     * NOTA: Esta versão simplificada não utiliza uma GUI para definir limites
     * Use o comando /pfunil definir <jogador> <limite> no lugar
     */
    public static void openLimitGUI(Player player, String targetName, PFunil plugin) {
        player.sendMessage(MessageUtil.format("&cEsta versão do plugin utiliza apenas comandos."));
        player.sendMessage(MessageUtil.format("&ePara definir um limite: &f/pfunil definir " + targetName + " <limite>"));
        player.sendMessage(MessageUtil.format("&ePara ver informações: &f/pfunil info " + targetName));
    }
}