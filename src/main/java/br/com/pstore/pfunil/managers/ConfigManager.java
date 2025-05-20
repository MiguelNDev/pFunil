package br.com.pstore.pfunil.managers;

import br.com.pstore.pfunil.PFunil;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Gerenciador de configurações do plugin
 */
public class ConfigManager {

    private final PFunil plugin;
    private FileConfiguration config;
    private int defaultHopperLimit;
    private boolean checkPlotsOnly;

    /**
     * Construtor da classe
     * @param plugin Instância do plugin
     */
    public ConfigManager(PFunil plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    /**
     * Carrega as configurações do plugin
     */
    private void loadConfig() {
        // Salva a configuração padrão se não existir
        plugin.saveDefaultConfig();
        
        // Carrega a configuração
        config = plugin.getConfig();
        
        // Carrega as configurações
        defaultHopperLimit = config.getInt("default-limit", 64);
        checkPlotsOnly = config.getBoolean("check-plots-only", true);
    }

    /**
     * Recarrega as configurações do plugin
     */
    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }

    /**
     * Obtém o limite padrão de funis
     * @return Limite padrão de funis
     */
    public int getDefaultHopperLimit() {
        return defaultHopperLimit;
    }

    /**
     * Define o limite padrão de funis
     * @param limit Limite padrão de funis
     */
    public void setDefaultHopperLimit(int limit) {
        defaultHopperLimit = limit;
        config.set("default-limit", limit);
        plugin.saveConfig();
    }

    /**
     * Verifica se o plugin deve verificar apenas em plots
     * @return true se o plugin deve verificar apenas em plots, false caso contrário
     */
    public boolean isCheckPlotsOnly() {
        return checkPlotsOnly;
    }

    /**
     * Define se o plugin deve verificar apenas em plots
     * @param checkPlotsOnly true se o plugin deve verificar apenas em plots, false caso contrário
     */
    public void setCheckPlotsOnly(boolean checkPlotsOnly) {
        this.checkPlotsOnly = checkPlotsOnly;
        config.set("check-plots-only", checkPlotsOnly);
        plugin.saveConfig();
    }
}
