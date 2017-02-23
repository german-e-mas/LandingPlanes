package mas.german.landingplanes.controller;

import mas.german.landingplanes.Game;
import mas.german.landingplanes.Position;
import mas.german.landingplanes.view.AerodromeView;

/**
 * Controller side of the MVC Pattern. This Controller class receives the information from the view
 * and modifies the Model accordingly.
 */
public class Controller implements AerodromeView.OnViewEventListener {
  private static final String TAG = Controller.class.getSimpleName();
  private static final int NONE_SELECTED = -1;

  // Game instance that contains the Model data.
  private Game mGame = Game.getInstance();

  private int mSelectedId = NONE_SELECTED;


  @Override
  public void onAerodromeTapped(Position position) {
    if (mSelectedId != NONE_SELECTED) {
      mGame.changeAircraftDirection(mSelectedId, position);
    }
    mSelectedId = NONE_SELECTED;
  }

  @Override
  public void onAircraftSelected(int id) {
    mSelectedId = id;
  }
}
