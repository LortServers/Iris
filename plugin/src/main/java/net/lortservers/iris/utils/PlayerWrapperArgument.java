package net.lortservers.iris.utils;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.captions.Caption;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.exceptions.parsing.NoInputProvidedException;
import cloud.commandframework.exceptions.parsing.ParserException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.screamingsandals.lib.player.PlayerMapper;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;

public class PlayerWrapperArgument<C> extends CommandArgument<C, PlayerWrapper> {
    private PlayerWrapperArgument(
            final boolean required,
            final @NonNull String name,
            final @NonNull String defaultValue,
            final @Nullable BiFunction<@NonNull CommandContext<C>,
                    @NonNull String, @NonNull List<@NonNull String>> suggestionsProvider,
            final @NonNull ArgumentDescription defaultDescription
    ) {
        super(required, name, new PlayerWrapperParser<>(), defaultValue, PlayerWrapper.class, suggestionsProvider, defaultDescription);
    }

    public static <C> @NonNull Builder<C> newBuilder(final @NonNull String name) {
        return new Builder<>(name);
    }

    public static <C> @NonNull CommandArgument<C, PlayerWrapper> of(final @NonNull String name) {
        return PlayerWrapperArgument.<C>newBuilder(name).asRequired().build();
    }

    public static <C> @NonNull CommandArgument<C, PlayerWrapper> optional(final @NonNull String name) {
        return PlayerWrapperArgument.<C>newBuilder(name).asOptional().build();
    }

    public static final class Builder<C> extends CommandArgument.Builder<C, PlayerWrapper> {
        private Builder(final @NonNull String name) {
            super(PlayerWrapper.class, name);
        }

        @Override
        public @NonNull PlayerWrapperArgument<C> build() {
            return new PlayerWrapperArgument<>(
                    this.isRequired(),
                    this.getName(),
                    this.getDefaultValue(),
                    this.getSuggestionsProvider(),
                    this.getDefaultDescription()
            );
        }
    }

    public static final class PlayerWrapperParser<C> implements ArgumentParser<C, PlayerWrapper> {
        @Override
        public @NonNull ArgumentParseResult<@NonNull PlayerWrapper> parse(
                @NonNull CommandContext<@NonNull C> commandContext,
                @NonNull Queue<@NonNull String> inputQueue
        ) {
            final String input = inputQueue.peek();
            if (input == null) {
                return ArgumentParseResult.failure(new NoInputProvidedException(
                        PlayerWrapperParser.class,
                        commandContext
                ));
            }

            final Optional<PlayerWrapper> player = PlayerMapper.getPlayer(input);
            inputQueue.remove();
            if (player.isPresent()) {
                inputQueue.remove();
                return ArgumentParseResult.success(player.orElseThrow());
            }
            return ArgumentParseResult.failure(new PlayerWrapperParseException(commandContext));
        }
    }

    public static final class PlayerWrapperParseException extends ParserException {
        public PlayerWrapperParseException(
                final @NonNull CommandContext<?> context
        ) {
            super(
                    PlayerWrapperParser.class,
                    context,
                    Caption.of("Invalid player!")
            );
        }
    }
}
