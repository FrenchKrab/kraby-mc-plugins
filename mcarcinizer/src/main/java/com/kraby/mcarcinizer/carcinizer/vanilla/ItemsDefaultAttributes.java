package com.kraby.mcarcinizer.carcinizer.vanilla;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemsDefaultAttributes {
    private static final String DATA_RESOURCE_PATH = "/items_default_attributes.yml";

    private static ItemsDefaultAttributes singleton;

    private final ConfigurationSection data;

    private ItemsDefaultAttributes() {
        InputStream ymlStream = this.getClass().getResourceAsStream(DATA_RESOURCE_PATH);
        InputStreamReader reader = new InputStreamReader(ymlStream);
        data = YamlConfiguration.loadConfiguration(reader);
    }

    /**
     * Get the singleton of this class.
     * @return
     */
    public static ItemsDefaultAttributes getSingleton() {
        if (singleton == null)
            singleton = new ItemsDefaultAttributes();
        return singleton;
    }


    /**
     * Get all the default item attributes that come from vanilla behaviour.
     * @param itemType
     * @return
     */
    public Map<Attribute, AttributeModifier> getItemAttributes(Material itemType) {
        String typeString = itemType.toString();
        if (!data.contains(typeString))
            return new HashMap<>();

        HashMap<Attribute, AttributeModifier> result = new HashMap<>();

        ConfigurationSection itemSection = data.getConfigurationSection(typeString);
        Set<String> attributes = itemSection.getKeys(false);
        for (String attStr : attributes) {
            Attribute att;
            try {
                att = Attribute.valueOf(attStr);
            } catch (Exception e) {
                continue;
            }

            AttributeModifier modifier = new AttributeModifier(
                UUID.randomUUID(),
                "default",
                itemSection.getDouble(attStr),
                Operation.ADD_NUMBER,
                itemType.getEquipmentSlot());
            result.put(att, modifier);
        }

        return result;
    }
}
