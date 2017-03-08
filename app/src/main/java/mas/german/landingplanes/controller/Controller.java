package mas.german.landingplanes.controller;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.Position;
import mas.german.landingplanes.view.GameView;

/**
 * Controller side of the MVC Pattern. This Controller class receives the information from the view
 * and modifies the Model accordingly.
 */
public class Controller implements GameView.ViewEventsListener {
  private static final String TAG = Controller.class.getSimpleName();

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

  @Override
  public void onAerodromeTapped(Position position) {
    // A position has been tapped. Tell the game to select an aircraft in it's position. If there's
    // none there, try to move a selected aircraft towards that position.
    if (!mGame.selectAircraftAtPosition(position)) {
      mGame.orientateSelectedAircraft(position);
    }
  }

  @Override
  public void onViewReady() {
    mGame.initialize();
  }

  @Override
  public void onRestartPressed() {
    mGame.initialize();
  }
}
