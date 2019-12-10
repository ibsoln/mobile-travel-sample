//
// Copyright (c) 2019 Couchbase, Inc All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package com.couchbase.travelsample.ui.controller;

import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.DefaultListModel;

import com.couchbase.travelsample.db.FlightsDao;
import com.couchbase.travelsample.db.LocalStore;
import com.couchbase.travelsample.model.Flight;
import com.couchbase.travelsample.ui.Nav;
import com.couchbase.travelsample.ui.view.FlightSearchView;
import com.couchbase.travelsample.ui.view.HotelSearchView;
import com.couchbase.travelsample.ui.view.UserView;


@Singleton
public final class UserController extends PageController {
    private final static Logger LOGGER = Logger.getLogger(UserController.class.getName());


    @Nonnull
    private final DefaultListModel<Flight> flightsModel = new DefaultListModel<>();

    @Nonnull
    private final FlightsDao flightsDao;

    @Inject
    public UserController(@Nonnull Nav nav, @Nonnull LocalStore localStore, @Nonnull FlightsDao flightsDao) {
        super(UserView.PAGE_NAME, nav, localStore);
        this.flightsDao = flightsDao;
    }

    @Nonnull
    public DefaultListModel<Flight> getFlightsModel() { return flightsModel; }

    public void fetchFlights() { flightsDao.getFlights(this::updateFlights); }

    public void selectFlight() { toPage(FlightSearchView.PAGE_NAME); }

    public void selectHotel() { toPage(HotelSearchView.PAGE_NAME); }

    @Override
    protected void onClose() { }

    private void updateFlights(List<Flight> flights) {
        flightsModel.clear();
        for (Flight flight : flights) { flightsModel.addElement(flight); }
    }
}
