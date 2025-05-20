package br.com.pstore.pfunil.managers;

import br.com.pstore.pfunil.PFunil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gerenciador de dados dos jogadores
 */
public class PlayerManager {

    private final PFunil plugin;
    private final Map<String, Integer> playerLimits;
    private final Map<String, Integer> hopperCounts;
    private final File playerDataFile;
    private FileConfiguration playerData;

    /**
     * Construtor da classe
     * @param plugin Instância do plugin
     */
    public PlayerManager(PFunil plugin) {
        this.plugin = plugin;
        this.playerLimits = new HashMap<>();
        this.hopperCounts = new HashMap<>();
        this.playerDataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        
        loadPlayerData();
    }

    /**
     * Carrega os dados dos jogadores do arquivo
     */
    private void loadPlayerData() {
        if (!playerDataFile.exists()) {
            try {
                playerDataFile.getParentFile().mkdirs();
                playerDataFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Não foi possível criar o arquivo de dados dos jogadores: " + e.getMessage());
            }
        }

        playerData = YamlConfiguration.loadConfiguration(playerDataFile);

        // Carrega os limites de funis
        if (playerData.contains("limits")) {
            for (String playerName : playerData.getConfigurationSection("limits").getKeys(false)) {
                int limit = playerData.getInt("limits." + playerName);
                playerLimits.put(playerName, limit);
            }
        }

        // Carrega a contagem de funis
        if (playerData.contains("hoppers")) {
            for (String playerName : playerData.getConfigurationSection("hoppers").getKeys(false)) {
                int count = playerData.getInt("hoppers." + playerName);
                hopperCounts.put(playerName, count);
            }
        }
    }

    /**
     * Salva os dados dos jogadores no arquivo
     */
    public void savePlayerData() {
        try {
            // Salva os limites de funis
            for (Map.Entry<String, Integer> entry : playerLimits.entrySet()) {
                playerData.set("limits." + entry.getKey(), entry.getValue());
            }

            // Salva a contagem de funis
            for (Map.Entry<String, Integer> entry : hopperCounts.entrySet()) {
                playerData.set("hoppers." + entry.getKey(), entry.getValue());
            }

            playerData.save(playerDataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível salvar o arquivo de dados dos jogadores: " + e.getMessage());
        }
    }

    /**
     * Obtém o limite de funis de um jogador
     * @param playerName Nome do jogador
     * @return Limite de funis
     */
    public int getPlayerLimit(String playerName) {
        return playerLimits.getOrDefault(playerName, plugin.getConfigManager().getDefaultHopperLimit());
    }

    /**
     * Define o limite de funis de um jogador
     * @param playerName Nome do jogador
     * @param limit Limite de funis
     */
    public void setPlayerLimit(String playerName, int limit) {
        playerLimits.put(playerName, limit);
        savePlayerData();
    }

    /**
     * Obtém a quantidade de funis de um jogador
     * @param playerName Nome do jogador
     * @return Quantidade de funis
     */
    public int getPlayerHopperCount(String playerName) {
        return hopperCounts.getOrDefault(playerName, 0);
    }

    /**
     * Incrementa a quantidade de funis de um jogador
     * @param playerName Nome do jogador
     */
    public void incrementHopperCount(String playerName) {
        int count = getPlayerHopperCount(playerName);
        hopperCounts.put(playerName, count + 1);
        savePlayerData();
    }

    /**
     * Decrementa a quantidade de funis de um jogador
     * @param playerName Nome do jogador
     */
    public void decrementHopperCount(String playerName) {
        int count = getPlayerHopperCount(playerName);
        if (count > 0) {
            hopperCounts.put(playerName, count - 1);
            savePlayerData();
        }
    }

    /**
     * Obtém todos os nomes de jogadores registrados
     * @return Lista com os nomes dos jogadores
     */
    public List<String> getAllPlayerNames() {
        List<String> players = new ArrayList<>();
        
        // Adiciona jogadores com limites definidos
        for (String player : playerLimits.keySet()) {
            if (!players.contains(player)) {
                players.add(player);
            }
        }
        
        // Adiciona jogadores com funis colocados
        for (String player : hopperCounts.keySet()) {
            if (!players.contains(player)) {
                players.add(player);
            }
        }
        
        return players;
    }

    /**
     * Salva todos os dados dos jogadores
     */
    public void saveAllPlayerData() {
        savePlayerData();
    }
}
