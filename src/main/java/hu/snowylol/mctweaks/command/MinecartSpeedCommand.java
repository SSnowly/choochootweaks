package hu.snowylol.mctweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import hu.snowylol.mctweaks.MinecartSpeedManager;
import hu.snowylol.mctweaks.config.MinecartSpeedConfig;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;

public class MinecartSpeedCommand {
    
    private static final SuggestionProvider<ServerCommandSource> MINECART_TYPE_SUGGESTIONS = 
        (context, builder) -> {
            Arrays.stream(MinecartSpeedConfig.MinecartType.values())
                .map(type -> type.getKey())
                .forEach(builder::suggest);
            return builder.buildFuture();
        };
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("minecartspeed")
            .requires(source -> source.hasPermissionLevel(2))
            .then(CommandManager.literal("set")
                .then(CommandManager.argument("type", StringArgumentType.word())
                    .suggests(MINECART_TYPE_SUGGESTIONS)
                    .then(CommandManager.argument("speed", DoubleArgumentType.doubleArg(0.0, 10000.0))
                        .executes(MinecartSpeedCommand::setSpeed))))
            .then(CommandManager.literal("get")
                .then(CommandManager.argument("type", StringArgumentType.word())
                    .suggests(MINECART_TYPE_SUGGESTIONS)
                    .executes(MinecartSpeedCommand::getSpeed)))
            .then(CommandManager.literal("list")
                .executes(MinecartSpeedCommand::listSpeeds))
            .then(CommandManager.literal("reset")
                .executes(MinecartSpeedCommand::resetSpeeds))
        );
    }
    
    private static int setSpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String typeKey = StringArgumentType.getString(context, "type");
        double speed = DoubleArgumentType.getDouble(context, "speed");
        
        MinecartSpeedConfig.MinecartType type = findMinecartType(typeKey);
        if (type == null) {
            context.getSource().sendError(Text.translatable("command.choochoo-tweaks.invalid_type", typeKey)
                .formatted(Formatting.RED));
            String validTypes = String.join(", ", 
                Arrays.stream(MinecartSpeedConfig.MinecartType.values())
                    .map(MinecartSpeedConfig.MinecartType::getKey)
                    .toArray(String[]::new));
            context.getSource().sendMessage(Text.translatable("command.choochoo-tweaks.valid_types", validTypes)
                .formatted(Formatting.YELLOW));
            return 0;
        }
        
        MinecartSpeedManager.setMinecartSpeed(type, speed, context.getSource().getServer());
        
        context.getSource().sendFeedback(() -> Text.translatable("command.choochoo-tweaks.set_speed", 
            type.getDisplayName(), String.format("%.2f", speed))
            .formatted(Formatting.GREEN), true);
        
        return 1;
    }
    
    private static int getSpeed(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String typeKey = StringArgumentType.getString(context, "type");
        
        MinecartSpeedConfig.MinecartType type = findMinecartType(typeKey);
        if (type == null) {
            context.getSource().sendError(Text.translatable("command.choochoo-tweaks.invalid_type", typeKey)
                .formatted(Formatting.RED));
            return 0;
        }
        
        MinecartSpeedConfig config = MinecartSpeedManager.getConfig(context.getSource().getServer());
        double speed = config.getSpeed(type);
        
        context.getSource().sendFeedback(() -> Text.translatable("command.choochoo-tweaks.get_speed", 
            type.getDisplayName(), String.format("%.2f", speed))
            .formatted(Formatting.GREEN), false);
        
        return 1;
    }
    
    private static int listSpeeds(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecartSpeedConfig config = MinecartSpeedManager.getConfig(context.getSource().getServer());
        
        context.getSource().sendFeedback(() -> Text.translatable("command.choochoo-tweaks.list_speeds")
            .formatted(Formatting.GOLD), false);
        
        for (MinecartSpeedConfig.MinecartType type : MinecartSpeedConfig.MinecartType.values()) {
            double speed = config.getSpeed(type);
            context.getSource().sendFeedback(() -> Text.literal("  ")
                .append(Text.literal(type.getDisplayName())
                    .formatted(Formatting.YELLOW))
                .append(Text.literal(": ")
                    .formatted(Formatting.WHITE))
                .append(Text.literal(String.format("%.2f", speed))
                    .formatted(Formatting.GREEN)), false);
        }
        
        return 1;
    }
    
    private static int resetSpeeds(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        MinecartSpeedConfig config = MinecartSpeedManager.getConfig(context.getSource().getServer());
        config.resetToDefaults();
        MinecartSpeedManager.saveConfig(context.getSource().getServer());
        
        context.getSource().sendFeedback(() -> Text.translatable("command.choochoo-tweaks.reset_speeds")
            .formatted(Formatting.GREEN), true);
        
        return 1;
    }
    
    private static MinecartSpeedConfig.MinecartType findMinecartType(String key) {
        for (MinecartSpeedConfig.MinecartType type : MinecartSpeedConfig.MinecartType.values()) {
            if (type.getKey().equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
