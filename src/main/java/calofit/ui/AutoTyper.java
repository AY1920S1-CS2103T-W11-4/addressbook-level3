package calofit.ui;

import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * Handle autotyping commands for the demo.
 * Binds to a {@link TextInputControl}.
 */
public class AutoTyper implements EventHandler<ActionEvent> {
    public static final List<String> DEMO_COMMANDS = List.of(
        "set 2200",
        "add n/ Spaghetti",
        "find Soup",
        "add 1",
        "suggest",
        "report"
    );
    public static final Duration DELAY = Duration.millis(30);

    private Iterator<String> nextCommandIterator = DEMO_COMMANDS.iterator();
    private Timeline typingAnim;
    //Need to keep method ref in a variable, so we can remove properly.
    private EventHandler<KeyEvent> keyHandler = this::filterKeyInput;

    /**
     * Called when animation finishes. Will unbind filters from control if no more autotyped commands.
     * @param control Bound control
     */
    private void endTimeline(TextInputControl control) {
        this.typingAnim = null;
        if (!nextCommandIterator.hasNext()) {
            control.removeEventFilter(KeyEvent.ANY, keyHandler);
            control.removeEventFilter(ActionEvent.ACTION, this);
        }
    }

    /**
     * Builds an typing animation {@link Timeline}.
     * @param control Text input control
     * @param nextCmd Command to fill in
     * @return Animation timeline
     */
    private Timeline buildTimeline(TextInputControl control, String nextCmd) {
        Timeline typingAnim = new Timeline();
        List<KeyFrame> keyFrames = typingAnim.getKeyFrames();

        Duration cur = DELAY;
        for (int i = 0; i < nextCmd.length(); i++, cur = cur.add(DELAY)) {
            //For every character, generate a KeyFrame that updates the text and cursor position.
            final int pos = i + 1;
            keyFrames.add(new KeyFrame(cur, evt -> {
                control.setText(nextCmd.substring(0, pos));
                control.positionCaret(pos);
            }));
        }
        //At the end of the animation, mark it as completed.
        keyFrames.add(new KeyFrame(cur, evt -> this.endTimeline(control)));

        return typingAnim;
    }

    @Override
    public void handle(ActionEvent event) {
        // If typing animation is playing, prevent user interaction
        if (typingAnim != null) {
            event.consume();
            return;
        }

        TextInputControl control = (TextInputControl) event.getSource();
        if (control.getText().isEmpty()) {
            typingAnim = buildTimeline(control, nextCommandIterator.next());
            typingAnim.play();
            event.consume();
        }
    }

    /**
     * Handles filtering of KeyEvents on the control.
     * @param event Key event
     */
    private void filterKeyInput(KeyEvent event) {
        //Allow only Enter key events
        if (event.getCode() != KeyCode.ENTER) {
            event.consume();
        }
    }

    /**
     * Binds to a {@link TextInputControl}, and adds the appropriate filters.
     * @param control Control to bind to.
     */
    public void bindTo(TextInputControl control) {
        control.addEventFilter(ActionEvent.ACTION, this);
        control.addEventFilter(KeyEvent.ANY, keyHandler);
    }
}
