package acme.features.manager.task;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Managers;
import acme.entities.tasks.Task;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractUpdateService;
import acme.services.SpamService;

@Service
public class ManagersTaskUpdateService implements AbstractUpdateService<Managers, Task>{
	
	@Autowired
	protected ManagersTaskRepository repository;
	
	@Autowired
	protected SpamService spam;
	
	@Override
	public boolean authorise(final Request<Task> request) {
		assert request != null;
		final boolean result;
		Task task;
		int taskId;
		Managers manager;
		Principal principal;
		
		taskId=request.getModel().getInteger("id");
		task=this.repository.findOneTaskById(taskId);
		manager = task.getManagers();
		principal = request.getPrincipal();
		result = manager.getUserAccount().getId() == principal.getAccountId();
		return result;
	}

	@Override
	public void bind(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
		
	}

	@Override
	public void unbind(final Request<Task> request, final Task entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "title", "begin", "end","description","link", "isPublic", "workload", "executionPeriod");
		model.setAttribute("readonly", false);
	}

	@Override
	public Task findOne(final Request<Task> request) {
		Task task;
		int taskId;
		
		taskId=request.getModel().getInteger("id");
		task=this.repository.findOneTaskById(taskId);
		return task;
	}

	@Override
	public void validate(final Request<Task> request, final Task entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		final Date now =new Date();
		final Date begin = entity.getBegin();
		final Date end = entity.getEnd();
		
		final boolean titleSpam = this.spam.isItSpam(entity.getTitle());
		final boolean descripcionSpam = this.spam.isItSpam(entity.getDescription());
		final int ent = (int) entity.getWorkload();
		final double dec = entity.getWorkload() - ent;
		
		if(!errors.hasErrors("begin") && !errors.hasErrors("end")) {
			errors.state(request, end.after(begin), "begin", "Managers.task.form.error.must-be-before-end");
		} 
		if(!errors.hasErrors("begin")) {
			errors.state(request, begin.after(now), "begin", "Managers.task.form.error.must-be-in-future");
		}
		if(!errors.hasErrors("end")) {
			errors.state(request, end.after(now), "end", "Managers.task.form.error.must-be-in-future");
		}
		if(!errors.hasErrors("begin") && !errors.hasErrors("end")) {
            errors.state(request, begin.before(end), "end", "Managers.task.form.error.must-be-after-begin");
        }
		if(!errors.hasErrors("begin")&&!errors.hasErrors("end")) {
			entity.setExecutionPeriod();
			final double periodo = entity.getExecutionPeriod(); 
			errors.state(request, periodo>=entity.getWorkload(), "workload", "Managers.task.form.error.must-be-less-than-work-period");
			errors.state(request, periodo>=entity.getWorkload(), "workload", "("+periodo+")");
		}
			errors.state(request, 0.59>=dec, "workload", "Managers.task.form.error.decimal-must-be-under-60");
			errors.state(request, !titleSpam, "title", "Managers.task.form.error.spam");
			errors.state(request, !descripcionSpam, "description", "Managers.task.form.error.spam");
		
	}

	@Override
	public void update(final Request<Task> request, final Task entity) {
		assert request != null;
		assert entity != null;
	
		entity.setExecutionPeriod();
		this.repository.save(entity);
		
	}

}
