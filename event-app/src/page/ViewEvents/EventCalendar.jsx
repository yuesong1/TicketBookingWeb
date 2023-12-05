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

const MockEvents = [];

function EventCalendar() {
  const [events, setEvents] = useState(MockEvents);
  const token = store.getToken();
  const history = useHistory();

  useEffect(() => {
    // Fetch events data from the API with the token in the header
    fetch(
      `${process.env.REACT_APP_API_URL}/view-event?mode=6months`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    }).then(response => response.json())
      .then((data) => setEvents(data))
      .catch((error) => console.error('Error fetching events:', error));
  }, [token]);

  const onClickBookTickets = (event) => history.push({
    pathname: `/bookTicket/${event.id}`,
    state: { event: event }
  })

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" sx={{ mt: 3, mb: 2 }}>
        Events in the next 6 months
      </Typography>
      <Grid container spacing={3}>
        {events.map((event) => (
          <Grid key={event.id} item xs={12} sm={6} md={4}>
            <Card>
              <CardHeader title={event.name} subheader={`${event.startDate} ~ ${event.endDate}`} />
              <CardContent>
                <Typography variant="body2" color="text.secondary">
                  {/* Planners: {JSON.stringify(event.planners)}<br /> */}
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
                    onClick={() => onClickBookTickets(event)}
                  >
                    Book Tickets
                  </Button>
                </div>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
}

export default EventCalendar;

