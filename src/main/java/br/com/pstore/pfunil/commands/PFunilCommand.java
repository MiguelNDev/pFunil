package br.com.pstore.pfunil.commands;

import br.com.pstore.pfunil.PFunil;
import br.com.pstore.pfunil.gui.AdminGUI;
import br.com.pstore.pfunil.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Comando principal do plugin
 */
public class PFunilCommand implements CommandExecutor {

    private final PFunil plugin;

    /**
     * Construtor da classe
     * @param plugin Instância do plugin
     */
    public PFunilCommand(PFunil plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Verifica se o comando foi executado por um jogador
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtil.format("&cEste comando só pode ser executado por jogadores."));
            return true;
        }

        Player player = (Player) sender;

        // Verifica se o jogador tem permissão para usar o comando
        if (!player.hasPermission("pfunil.admin")) {
            player.sendMessage(MessageUtil.format("&cVocê não tem permissão para usar este comando."));
            return true;
        }

        // Se tiver argumentos, verifica as subcomandos
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.getConfigManager().reloadConfig();
                player.sendMessage(MessageUtil.format("&aConfiguração recarregada com sucesso!"));
                return true;
            }
            
            if (args[0].equalsIgnoreCase("definir") && args.length == 3) {
                String playerName = args[1];
                try {
                    int limit = Integer.parseInt(args[2]);
                    if (limit < 0) {
                        player.sendMessage(MessageUtil.format("&cO limite deve ser um número positivo."));
                        return true;
                    }
                    
                    plugin.getPlayerManager().setPlayerLimit(playerName, limit);
                    player.sendMessage(MessageUtil.format("&aLimite de funis para " + playerName + " definido para " + limit));
                } catch (NumberFormatException e) {
                    player.sendMessage(MessageUtil.format("&cO limite deve ser um número válido."));
                }
                return true;
            }
            
            if (args[0].equalsIgnoreCase("info") && args.length == 2) {
                String playerName = args[1];
                int limit = plugin.getPlayerManager().getPlayerLimit(playerName);
                int used = plugin.getPlayerManager().getPlayerHopperCount(playerName);
                
                player.sendMessage(MessageUtil.format("&aInformações de " + playerName + ":"));
                player.sendMessage(MessageUtil.format("&7Limite de funis: &f" + limit));
                player.sendMessage(MessageUtil.format("&7Funis utilizados: &f" + used));
                return true;
            }
        }
        
        // Se não tiver argumentos ou comando não reconhecido, abre a GUI
        AdminGUI.openAdminGUI(player, plugin);
        
        return true;
    }
}
