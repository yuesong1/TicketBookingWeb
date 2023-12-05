import React, { useState, useEffect } from 'react';
import {
  Button,
  TextField,
} from '@mui/material';
import { Link, useParams, useHistory } from 'react-router-dom';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import './EditEvent.css'; // Assuming you have a separate CSS file for EditEvent
import store from '../../component/catche/store';
import formatDateToCustomFormat from '../../component/helper/Localdate';
import parseDateTimeString from '../../component/helper/parseDate';

const mockEvent = {
  artist: "",
  description: "",
  endDate: "2000-00-00 00:00",
  name: "",
  planners: [],
  startDate: "2000-00-00 00:00",
  version:0,
  venue:{
    address: "",
    id: "",
    name: "",
    version: 0,
    sections:
      [
     
    ]
  },
}

function EditEvent() {
  const [eventData, setEventData] = useState(mockEvent);
  const [ticketPrices, setTicketPrices] = useState({});
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedEndDate, setSelectedEndDate] = useState(null);
  const [eventName, setEventName] = useState('');
  const [eventArtist, setEventArtist] = useState('');
  const [eventDescription, setEventDescription] = useState('');
  const [eventPlanners, setEventPlanners] = useState('');
  const [version, setVersion] = useState(0);
  const history = useHistory();
  const { eventId } = useParams();
  const token = store.getToken();
  const myemail = store.getEmail();

  
  

  const handleEventPlannersChange = (event) => {
    setEventPlanners(event.target.value);
  };


  useEffect(() => {
    setEventName(eventData.name);
    setEventDescription(eventData.description);
    setEventArtist(eventData.artist);
    setSelectedDate(parseDateTimeString(eventData.startDate));
    setSelectedEndDate(parseDateTimeString(eventData.endDate));
    setVersion(eventData.version);
    setEventPlanners(eventData.planners.join(', ')); // Convert array to string
    if (eventData && eventData.venue && eventData.venue.sections) {
      const initialTicketPrices = {};
      eventData.venue.sections.forEach((section) => {
        initialTicketPrices[section.type] = {
          price: section.price,
        };
      });
      setTicketPrices(initialTicketPrices);
    }
  }, [eventData]);

  useEffect(() => {
    // Fetch event data from the API based on eventId with the token in the header
    fetch(
      `${process.env.REACT_APP_API_URL}/view-event?mode=one&id=${eventId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
      },
    })
      .then((response) => { 
        if (response.status === 200) {
          console.log(response.json);
          return response.json();
          
        } else {
          throw new Error('Unauthorized');
        }
      })
      .then((data) => setEventData(data))
      .catch((error) => console.error('Error fetching event data:', error));
  }, [eventId, token]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const plannerEmails = eventPlanners.split(',').map((email) => email.trim());
    plannerEmails.push(myemail);

    const validPlannerEmails = plannerEmails.filter(isValidEmail);
    try {
      // Prepare data for submission
      const eventDetails = {
        id: eventId,
        name: eventName,
        artist: eventArtist,
        planners: validPlannerEmails,
        description: eventDescription,
        version:version,
        venue: {
          ...eventData.venue,
          sections: eventData.venue.sections.map((section) => ({
            ...section,
            price: ticketPrices[section.type]?.price || '',
          })),
        },
        startdate: formatDateToCustomFormat(selectedDate), // Convert to ISO string format
        enddate: formatDateToCustomFormat(selectedEndDate),
      };

      console.log(eventDetails)
      // Send the data to your API endpoint
      const response = await fetch(
        process.env.REACT_APP_API_URL != null ? `${process.env.REACT_APP_API_URL}/update-event` : 
        `http://localhost:8080/event_backend_war_exploded/update-event`, {
        method: 'PUT', // Assuming this is how you update an event
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(eventDetails),
      });
      if (response.ok) {
        alert('Event updated successfully');
        history.push(`/viewEvents/byPlanner/${store.getEmail()}`)
      } else {
        if (response.status === 400) {
            alert('Cannot update the event as there are existing bookings associated with it.');
        } else if (response.status === 405) {
            alert('The event was modified by someone else. Please reload and try again.');
            history.push(`/viewEvents/byPlanner/${store.getEmail()}`)
        } else {
            const responseBody = await response.json();
            const errorMsg = responseBody.error ? responseBody.error : 'An error occurred';
            console.error('Failed to update event:', errorMsg);
            alert(errorMsg);
        }
    }
    } catch (error) {
        console.error('An error occurred', error);
    }
  };

  function isValidEmail(email) {
    // Regular expression for simple email validation
    const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return regex.test(email);
}

  const handleTicketPriceChange = (section, newPrice) => {
    setTicketPrices((prevTicketPrices) => ({
      ...prevTicketPrices,
      [section.type]: { price: parseInt(newPrice, 10) },
    }));
  };

  const handleEventNameChange = (event) => {
    setEventName(event.target.value);
  };

  const handleDateChange = (date) => {
    setSelectedDate(date.toISOString());
  };

  const handleEndDateChange = (date) => {
    setSelectedEndDate(date.toISOString());
  };

  const handleEventDescriptionChange = (event) => {
    setEventDescription(event.target.value);
  };

  return (
    <div className="create-event-container">
      <h2>Edit Event</h2>

      <div className="event-details-container">
        <TextField
          label="Event Name"
          value={eventName}
          onChange={handleEventNameChange}
          fullWidth
          sx={{ marginBottom: 2 }}
        />
        <TextField
          label="Event Description"
          value={eventDescription}
          onChange={handleEventDescriptionChange}
          fullWidth
          multiline
          rows={4}
          sx={{ marginBottom: 2 }}
        />

        <TextField
          label="Event Planners (comma-separated emails)"
          value={eventPlanners}
          onChange={handleEventPlannersChange}
          fullWidth
          sx={{ marginBottom: 2 }}
        />
      </div>

      <div className="ticket-sections-container">
        <h3>Selected Venue: {eventData.venue?.name}</h3>
        {eventData.venue?.sections.map((section) => (
          <div key={section.id} className="ticket-section">
            <div className="ticket-line">
              <span className="ticket-label">Price of {section.type}:</span>
              <TextField
                value={ticketPrices[section.type]?.price || ''}
                type="number"
                onChange={(e) =>
                  handleTicketPriceChange(section, e.target.value)
                }
                className="ticket-price-input"
              />
            </div>
          </div>
        ))}
      </div>

      <div className="date-picker-container">
        <h3>Choose Date and time </h3>
        <LocalizationProvider dateAdapter={AdapterDayjs}>
          Start Time:
          <DateTimePicker
            label="Start Time"
            value={selectedDate}
            onChange={handleDateChange}
          />

          End Time:
          <DateTimePicker
            label="End Time"
            value={selectedEndDate}
            onChange={handleEndDateChange}
          />
        </LocalizationProvider>
      </div>

      <div className="button-container">
        <Button variant="contained" color="primary" onClick={handleSubmit}>
          Update Event
        </Button>
        <Link to = {`/viewEvents/byPlanner/${store.getEmail()}`}>
          <Button>Back to Events</Button>
        </Link>
      </div>
    </div>
  );
}

export default EditEvent;
