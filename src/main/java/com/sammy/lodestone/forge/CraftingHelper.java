package com.sammy.lodestone.forge;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CraftingHelper {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static ItemStack getItemStack(JsonObject json, boolean readNBT) {
        return getItemStack(json, readNBT, false);
    }

    public static Item getItem(String itemName, boolean disallowsAirInRecipe) {
        Item item = tryGetItem(itemName, disallowsAirInRecipe);
        if (item == null) {
            if (!Registries.ITEM.containsId(new Identifier(itemName)))
                throw new JsonSyntaxException("Unknown item '" + itemName + "'");
            if (disallowsAirInRecipe && item == Items.AIR)
                throw new JsonSyntaxException("Invalid item: " + itemName);
        }
        return Objects.requireNonNull(item);
    }

    @Nullable
    public static Item tryGetItem(String itemName, boolean disallowsAirInRecipe) {
        Identifier itemKey = new Identifier(itemName);
        if (!Registries.ITEM.containsId(itemKey))
            return null;

        Item item = Registries.ITEM.get(itemKey);
        if (disallowsAirInRecipe && item == Items.AIR)
            return null;
        return item;
    }

    public static ItemStack getItemStack(JsonObject json, boolean readNBT, boolean disallowsAirInRecipe) {
        String itemName = JsonHelper.getString(json, "item");
        Item item = getItem(itemName, disallowsAirInRecipe);
        if (readNBT && json.has("nbt")) {
            NbtCompound nbt = getNBT(json.get("nbt"));
            NbtCompound tmp = new NbtCompound();
            if (nbt.contains("ForgeCaps"))
            {
                tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                nbt.remove("ForgeCaps");
            }

            tmp.put("tag", nbt);
            tmp.putString("id", itemName);
            tmp.putInt("Count", JsonHelper.getInt(json, "count", 1));

            return ItemStack.fromNbt(tmp);
        }
        return new ItemStack(item, JsonHelper.getInt(json, "count", 1));
    }

    public static NbtCompound getNBT(JsonElement element) {
        try {
            if (element.isJsonObject())
                return StringNbtReader.parse(GSON.toJson(element));
            else
                return StringNbtReader.parse(JsonHelper.asString(element, "nbt"));
        }
        catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e);
        }
    }
}
