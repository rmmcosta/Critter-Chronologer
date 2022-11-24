drop table testschema.country;
select * from testeschema.country;

Select e.*  
from employee e  
where not exists ( select 1  
			from schedule_employee se  
			inner join schedule s  
			on s.id = se.schedule_id  
			where :startTime between s.start_time and s.end_time  
			or :endTime between s.start_time and s.end_time=