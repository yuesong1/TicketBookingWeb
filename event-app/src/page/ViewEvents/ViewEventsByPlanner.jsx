import React, { useEffect, useState } from 'react';
import {
  Button,
  Card,
  CardContent,
  CardHeader,
  Container,
  Grid,
  Typography,
} from '@mui/material';
import { useHistory } from 'react-router-dom';
import './ViewEvents.css';
import store from '../../component/catche/store';
import { useParams } from "react-router-dom/cjs/react-router-dom.min";

const MockEvents = [];

function ViewEventsByPlanner() {
  const [events, setEvents] = useState(MockEvents);
  const { plannerEmail } = useParams();
  const [searchInput, setSearchInput] = useState(''); // State to store the search input
  const userRole = store.getRole();
  const token = store.getToken();
  const history = useHistory();

  useEffect(() => {
    // Fetch events data from the API with the token in the header
    fetch(
      `${process.env.REACT_APP_API_URL}/view-event?mode=byPlanner&id=${plannerEmail}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    }).then((response) => {
      if (response.status === 200) {
        return response.json();
      } else {
        throw new Error('Unauthorized');
      }
    })
      .then((data) => setEvents(data))
      .catch((error) => console.error('Error fetching events:', error));
  }, [token]);

  const onClickCancelEvent = async (eventID) => {
    fetch(
      `${process.env.REACT_APP_API_URL}/delete-event?id=${eventID}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
    .then(res => {
      if (!res.ok) { // Check if response was successful
        return res.json().then(data => {
          // Use the JSON data to throw a more detailed error
          throw new Error(`Error status: ${res.status}. Message: ${data.message || 'Unknown error'}`);
        });
      }
  
      console.log('Event canceled successfully');
      setEvents(events => events.filter(e => e.id != eventID))
    })
    .catch(err => {
      console.error('Failed to cancel event', err);
      alert("Unable to cancel the event, booking exist");
      history.push(`/viewBookings/byEvent/${eventID}`);
    });
  };
  

  const onClickEditEvent = (event) => {
    history.push({
      pathname: `/EditEvent/${event.id}`,
      state: { event: event }
    });
  }

  const onClickViewBookings = (eID) => history.push(`/viewBookings/byEvent/${eID}`);

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        Planner {plannerEmail}'s Events
      </Typography>
      <Grid container spacing={3}>
        {events.map((event) => (
          <Grid key={event.id} item xs={12} sm={6} md={4}>
            <Card>
              <CardHeader title={event.name} subheader={`${event.startDate} ~ ${event.endDate}`} />
              <CardContent>
                <Typography variant="body2" color="text.secondary">
                  Planners: {JSON.stringify(event.planners)}<br />
                  Venue: {event.venue.name}<br />
                  Address: {event.venue.address}<br />
                  Artist: {event.artist}<br />
                  Description: {event.description}<br />
                  {/* Sections: {event.venue.sections.map(s => {
                    return <p>- {s.type} (${s.price}): {s.ticketLeft}/{s.capacity}</p>
                  })} */}
                </Typography>
                <div className='button-section'>
                  <Button variant="contained" color="primary"
                    onClick={() => onClickViewBookings(event.id)}
                  >
                    View Bookings
                  </Button>
                  {store.getRole() === "planner" && <>
                    <Button variant="contained" color="primary"
                      onClick={() => onClickEditEvent(event)}
                    >
                      Edit
                    </Button>
                    <Button variant="contained" color="primary"
                      onClick={() => onClickCancelEvent(event.id)}
                    >
                      Cancel
                    </Button>
                  </>}
                </div>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default ViewEventsByPlanner;

