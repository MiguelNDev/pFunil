package br.com.pstore.pfunil.listeners;

import br.com.pstore.pfunil.PFunil;
import br.com.pstore.pfunil.util.MessageUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listener para controlar a colocação de funis e interação com a GUI
 */
public class HopperListener implements Listener {

    private final PFunil plugin;

    /**
     * Construtor da classe
     * @param plugin Instância do plugin
     */
    public HopperListener(PFunil plugin) {
        this.plugin = plugin;
    }

    /**
     * Evento disparado quando um jogador tenta colocar um bloco
     * @param event Evento de colocação de bloco
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        // Verifica se o bloco é um funil
        if (block.getType() != Material.HOPPER) {
            return;
        }
        
        // Verifica se o jogador tem permissão para ignorar o limite
        if (player.hasPermission("pfunil.bypass")) {
            return;
        }
        
        // Verifica se o bloco está em um plot
        Object plot = getPlotAt(block);
        if (plot == null) {
            // Se o administrador configurou para verificar apenas em plots
            if (plugin.getConfigManager().isCheckPlotsOnly()) {
                return;
            }
        } else {
            // Em produção, verifica se o jogador é dono ou membro do plot usando a API do PlotSquared
            // Temporariamente sempre permitindo, para fins de compilação
            plugin.getLogger().info("Verificando se " + player.getName() + " é dono ou membro do plot");
        }
        
        // Obtém o limite do jogador
        String playerName = player.getName();
        int limit = plugin.getPlayerManager().getPlayerLimit(playerName);
        int currentCount = plugin.getPlayerManager().getPlayerHopperCount(playerName);
        
        // Verifica se o jogador atingiu o limite
        if (currentCount >= limit) {
            event.setCancelled(true);
            player.sendMessage(MessageUtil.format("&cVocê atingiu o limite de " + limit + " funis."));
            return;
        }
        
        // Incrementa o contador de funis do jogador
        plugin.getPlayerManager().incrementHopperCount(playerName);
        
        // Notifica o jogador se estiver próximo ao limite
        int remaining = limit - currentCount - 1;
        if (remaining <= 5 && remaining > 0) {
            player.sendMessage(MessageUtil.format("&eVocê tem apenas " + remaining + " funis restantes."));
        }
    }

    /**
     * Evento disparado quando um jogador interage com um inventário
     * @param event Evento de clique em inventário
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        String title = event.getView().getTitle();
        
        // Verifica se o inventário é a GUI do plugin
        if (title.equals("§8PFunil - Gerenciamento")) {
            event.setCancelled(true);
            
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }
            
            int slot = event.getSlot();
            
            // Recarregar configuração
            if (slot == 0 && clickedItem.getType() == Material.HOPPER) {
                plugin.getConfigManager().reloadConfig();
                player.sendMessage(MessageUtil.format("&aConfiguração recarregada com sucesso!"));
                player.closeInventory();
                return;
            }
            
            // Lista de jogadores
            if (slot == 2 && clickedItem.getType() == Material.CHEST) {
                // Mostrar uma lista de jogadores (via comando, não via GUI)
                player.closeInventory();
                player.sendMessage(MessageUtil.format("&aJogadores registrados:"));
                for (String playerName : plugin.getPlayerManager().getAllPlayerNames()) {
                    int playerLimit = plugin.getPlayerManager().getPlayerLimit(playerName);
                    int used = plugin.getPlayerManager().getPlayerHopperCount(playerName);
                    player.sendMessage(MessageUtil.format("&f" + playerName + ": &e" + used + "&7/&e" + playerLimit + " &7funis"));
                }
                return;
            }
            
            // Definir limite para jogador específico
            if (slot == 6 && clickedItem.getType() == Material.PAPER) {
                player.closeInventory();
                player.sendMessage(MessageUtil.format("&ePara definir um limite: &f/pfunil definir <jogador> <limite>"));
                return;
            }
            
            // Verificar jogador
            if (slot == 8 && clickedItem.getType() == Material.COMPASS) {
                player.closeInventory();
                player.sendMessage(MessageUtil.format("&ePara verificar um jogador: &f/pfunil info <jogador>"));
                return;
            }
        }
    }
    
    /**
     * Obtém o plot em que um bloco está localizado
     * @param block Bloco a ser verificado
     * @return Plot em que o bloco está ou null se não estiver em nenhum plot
     */
    private Object getPlotAt(Block block) {
        try {
            // Implementação temporária para compilação
            // Na produção, usará a API do PlotSquared
            plugin.getLogger().info("Verificando plot para bloco em: " + 
                    block.getWorld().getName() + ", " + 
                    block.getX() + ", " + 
                    block.getY() + ", " + 
                    block.getZ());
            
            // Retornar null temporariamente
            return null;
        } catch (Exception e) {
            plugin.getLogger().warning("Erro ao verificar plot: " + e.getMessage());
            return null;
        }
    }
}