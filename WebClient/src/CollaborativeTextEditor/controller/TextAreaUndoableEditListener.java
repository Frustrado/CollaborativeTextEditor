package CollaborativeTextEditor.controller;

import CollaborativeTextEditor.model.MyModel;
import CollaborativeTextEditor.view.MyView;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.undo.UndoableEdit;

public class TextAreaUndoableEditListener implements UndoableEditListener {
    private MyController controller;
    private MyView view;
    private MyModel model;

    TextAreaUndoableEditListener(MyController myController) {
        super();
        controller = myController;
        view = myController.view;
        model = myController.model;
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {

        if (controller.updateable) {
            if(model.getCaretPosition()==Integer.parseInt(controller.info[1])) {
                if (controller.info[2].equals("")) {
                        model.setCaretPosition(Integer.parseInt(controller.info[0]));
                    }
            } else {
                if (model.getCaretPosition() == Integer.parseInt(controller.info[0])) {
                    if (!controller.info[2].equals("")) {
                        model.setCaretPosition(Integer.parseInt(controller.info[1]));
                    }
                }
            }
            return;
            }

        System.out.println("Undoable");
        String changeText = "";
        UndoableEdit edit = e.getEdit();
        AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent) edit;
        int offset = event.getOffset();
        int lenght = event.getLength();
        try {
            changeText = event.getDocument().getText(offset, lenght);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
        System.out.println("#"+changeText+"#");

        controller.currentCursor = model.getCaretPosition();
        System.out.println(controller.currentCursor);
        System.out.println("curretCursor");
        try {
            controller.currentIndex[0] = model.getLineOfOffset(controller.currentCursor);
            controller.currentIndex[1] = model.getLineStartOffset(controller.currentIndex[0]);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        System.out.println(controller.currentIndex[0]);
        System.out.println("currentIndex[0]");
        System.out.println(controller.currentIndex[1]);
        System.out.println("currentIndex[1]");
        controller.updateable = true;
        e.getEdit().undo();
        controller.updateable = false;
        String msg;
        if (!controller.removalFlag) {
            System.out.println("prev curent");
            msg = ("Z" + (controller.previousIndex[0] + 1) + '.' + (controller.previousCursor - controller.previousIndex[1]) + '.' + (controller.currentIndex[0] + 1) + '.' + (controller.currentCursor - controller.currentIndex[1]) + ':' + changeText);
        } else {
            msg = ("Z" + (controller.currentIndex[0] + 1) + '.' + (controller.currentCursor - controller.currentIndex[1]) + '.' + (controller.previousIndex[0] + 1) + '.' + (controller.previousCursor - controller.previousIndex[1]) + ':');
            System.out.println("curent prev");
        }
        controller.mySendMessage(msg);

        System.out.println("kappa");


    }

}
