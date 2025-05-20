package br.com.pstore.pfunil;

import br.com.pstore.pfunil.commands.PFunilCommand;
import br.com.pstore.pfunil.listeners.HopperListener;
import br.com.pstore.pfunil.managers.ConfigManager;
import br.com.pstore.pfunil.managers.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Classe principal do plugin PFunil
 * Plugin para limitar a quantidade de funis (hoppers) por jogador em servidores com PlotSquared
 * 
 * @author pStore
 */
public class PFunil extends JavaPlugin {

    private static PFunil instance;
    private ConfigManager configManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        // Inicializa a instância
        instance = this;
        
        // Verifica se o PlotSquared está disponível
        if (getServer().getPluginManager().getPlugin("PlotSquared") == null) {
            getLogger().warning("PlotSquared não encontrado! Algumas funcionalidades podem estar limitadas.");
            // Em ambiente de desenvolvimento, continuamos mesmo sem o PlotSquared
            // Em produção, isto deveria desativar o plugin
        }
        
        // Inicializa os gerenciadores
        this.configManager = new ConfigManager(this);
        this.playerManager = new PlayerManager(this);
        
        // Registra eventos
        getServer().getPluginManager().registerEvents(new HopperListener(this), this);
        
        // Registra comandos
        getCommand("pfunil").setExecutor(new PFunilCommand(this));
        
        getLogger().info("PFunil foi ativado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Salva os dados dos jogadores
        if (playerManager != null) {
            playerManager.saveAllPlayerData();
        }
        
        getLogger().info("PFunil foi desativado com sucesso!");
    }

    /**
     * Obtém a instância do plugin
     * @return Instância do plugin
     */
    public static PFunil getInstance() {
        return instance;
    }

    /**
     * Obtém o gerenciador de configurações
     * @return Gerenciador de configurações
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Obtém o gerenciador de jogadores
     * @return Gerenciador de jogadores
     */
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
