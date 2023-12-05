import React from 'react';
import { BrowserRouter as Router, Route, Switch} from 'react-router-dom'; // Import BrowserRouter and Route
import Login from './page/Login/Login';
import SignUp from './page/SignUp/SignUp'; // Import the SignUp component
import CreateVenue from './page/CreateVenue/CreateVenue';
import ResponsiveAppBar from './component/topBar';
import ViewVenues from './page/ViewVenues/ViewVenues';
import CreateEvent from './page/CreateEvent/CreateEvent';
import ViewEvents from './page/ViewEvents/ViewEvents';
import ViewEventsByPlanner from './page/ViewEvents/ViewEventsByPlanner';
import BookTicket from './page/BookTicket/BookTicket';
import EditVenue from './page/EditVenue/EditVenue';
import EditEvent from './page/EditEvent/EditEvent';
import Payment from './page/Payment/Payment';
import ViewUsers from './page/ViewUsers/ViewUsers';
// import ViewBookings from "./page/ViewBookings/ViewBookings";
import ViewBookingsByCustomer from "./page/ViewBookings/ViewBookingsByCustomer";
import ViewEventsAll from "./page/ViewEvents/ViewEventsAll";
import ViewBookingsByEvent from "./page/ViewBookings/ViewBookingsByEvent";
import SearchEvents from "./page/ViewEvents/SearchEvents";
import EventCalendar from "./page/ViewEvents/EventCalendar";

function App() {
  return (
    <div className="App">
      
      <Router>
      <ResponsiveAppBar/>
        <Switch>
          <Route path="/" exact component={Login} /> {/* Login page route */}
          <Route path="/login" exact component={Login} /> {/* Login page route */}
          <Route path="/signup" component={SignUp} /> {/* SignUp page route */}
          <Route path="/createVenue" component={CreateVenue} /> {/* create venue page route */}
          <Route path="/viewVenues" component={ViewVenues} /> {/* View venue page route */}
          <Route path="/EditVenue/:id" component={EditVenue} /> {/* Edit venue page route */}
          <Route path="/createEvent" component={CreateEvent} /> {/* create event page route */}
          <Route path="/EditEvent/:eventId" component={EditEvent} /> {/* edit event page route */}
          {/*<Route path="/viewEvents" component={ViewEvents} /> // View event page route */}
          <Route path="/viewEvents/byPlanner/:plannerEmail" component={ViewEventsByPlanner} /> {/* View event page route */}
          <Route path="/viewEvents/all" component={ViewEventsAll} /> {/* View event page route */}
          <Route path="/searchEvents/" component={SearchEvents} /> {/* View event page route */}
          <Route path="/EventCalendar/" component={EventCalendar} /> {/* View event page route */}
          <Route path="/bookTicket/:id" component={BookTicket} /> {/* book ticket page route */}
          <Route path="/payment/:id" component={Payment} /> {/* payment route */}
          {/* <Route path="/viewBookings" component={ViewBookings} /> */}
          <Route path="/viewBookings/byCustomer/:customerEmail" component={ViewBookingsByCustomer} />
          <Route path="/viewBookings/byEvent/:eventID" component={ViewBookingsByEvent} />
          <Route path="/viewUsers" component={ViewUsers} /> 
        </Switch>
      </Router>
    </div>
  );
}

export default App;
