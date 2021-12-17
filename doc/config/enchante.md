# Better Anvil

## Markers

Markers are tools to "mark" items. It can be to mark lore (marker applied to show an item is "legendary" for example) or to control enchantment progression (apply a marker that is marked incompatible with any enchantment).

### Structure

```yml
markers:
	marker1:
		#...
	marker2:
		#...
```


### Markers properties

- **lore** :
	- **default** : no lore
	- **description** : the lore line that will be added
	- **accepted** :
		A dictionary of the form :
		```yml
		{
                0: '☖ {LVL}000-legged creature ☖',
                5: '☖ {LVL}000-legged monster ☖',
                10: '☖ Eternal Centipede ☖'
		}
		```
		Where keys indicate which lore line appear at each levels.
- **cap** :
	- **default** : 1
	- **description** : How many times this marker can be applied (won't block the items for applying an attchantment with this marker).
- **block_on_cap**
	- **default** : false
	- **description** : if this marker's cap is reached, attchantments that should apply this marker won't apply them.


## Attchantments

### attribute_enchants structure

- The item names (ITEM1, ITEM2) should be taken from https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
- attchantments names are user defined

```yml
attribute_enchants:
    ITEM1:
        attchantname1:
			#...
		attchantname2:
			#...
	ITEM2:
        attchantname3:
			#...
		attchantname4:
			#...
```

### Individual attchantment configuration

- **effect**
	- **description** : Effect applied (attribute or enchantment)
	- **accepted** : 
		- any attribute : https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html
		- any enchantment (in lowercase, eg `bane_of_arthropods`): https://www.digminecraft.com/lists/enchantment_list_pc.php
- **type**
	- **default** : `add`
	- **description** : minecraft's cumulation operation for this attributed modifier (do not influence this plugin's cumulation). eg : +1.2 move speed vs +120% move speed.
	- **accepted** :
		- `add`
		- `mult`
- **slot**
	- **default** : `ANY`
	- **description** : The slot the item has to fit in 
	- **accepted** : https://hub.spigotmc.org/javadocs/spigot/org/bukkit/inventory/EquipmentSlot.html or `ANY`
- **value**
	- **default** : `1.0`
	- **description** : The center of the distribution
- **deviation**
	- **default** : `0.0`
	- **description** : Deviation of the distribution (using gaussian distribution)
- **force_cost**
	- **default** : `false`
	- **description** : Force to use a fixed level cost ? If set to false, uses item's current cost.
- **cost**
	- **default** : `0`
	- **description** : Only used if `force_cost` is true, fixed cost value. Cost in levels. 
- **repair_raise**
	- **default** : `0`
	- **description** : How much this enchant raises the cost value.
- **count**
	- **default** : `1`
	- **description** : How many items needed to trigger this attchantment.
- **cumulation**
	- **default** : `CURRENT + ((1+pos(CURRENT)) * BONUS)` if multiplicative type, `CURRENT + BONUS` else.
	- **description** : How to cumulate effects when you stack attributes/enchants.
	- **accepted** :
		- Needs to be a valid formula with following variables :
		- `CURRENT` : current value
		- `BONUS` : new value
- **probability** : 
	- **default** : `1.0`
	- **description** : Chance for this effect to appear (between 0.0 and 1.0)
- **reroll**
	- **default** : `ATTCHANT_COUNT_ITEM`
	- **description** : How to handle effects reroll (reroll the value and wether it appears or not).
	- **accepted** :
		- `NO_LIMIT` : rerolled each time the consumable is put in the anvil
    	- `ITEM_CHANGED` : rerolled each time the item changes (enchant, name, attributes))
    	- `ATTCHANT_COUNT_ITEM` : rerolled each time the item is "attribute enchanted" (using Enchanté!'s enchantment system)
- **min_repair_cost**
	- **default** : `0`
	- **description** : Minimum repair cost the item needs to have be able to trigger this effect.
- **max_repair_cost**
	- **default** : `2147483647`
	- **description** : Maximum repair cost the item needs to have be able to trigger this effect.
- **works_on**
	- **default** : `[WEAPONS, TOOLS, ARMORS]`
	- **description** : list of items/item categories that are compatible with this effect
	- **accepted**
		- `WEAPONS` : any weapon of any material (sword, trident, bow, crossbow)
		- `TOOLS` : any tool of any material (axe, shovel, hoe, pickaxe, fishing rod)
		- `SHOVEL` / `AXE` / `PICKAXE` / `HOE` : item of this type with any material
		- `ARMORS` : any armor of any material
		- `OBJECTS` : anything that isn't above
		- Individual items : https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html
- **markers**
	- **default** : []
	- **description** : list of markers applied by this attchantment.
- **conflicts**
	- **default** : [lock]
	- **description** : name of the markers this effect is not compatible with. Default is an undefined `lock` honor so that it is easy to block all attchantments.