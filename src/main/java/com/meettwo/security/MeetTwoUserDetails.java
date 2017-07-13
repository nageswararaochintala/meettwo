package com.meettwo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.meettwo.model.SubAdminPermission;
import com.meettwo.model.User;
import com.meettwo.model.UserRole;


/**
 * @author Nageswara rao.ch
 *
 */
public class MeetTwoUserDetails extends User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	List<String> subscriptionPermissions;
	public MeetTwoUserDetails(User user,List<String> subscriptionPermissions) {
		super(user);
		this.subscriptionPermissions=subscriptionPermissions;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		final UserRole userRole = getUserRole();
		final List<SubAdminPermission> subAdminPermissions=getSubAdminPermissions();
		List<GrantedAuthority> roleAuthorities = new ArrayList<GrantedAuthority>();
		if(userRole!=null){
				
			GrantedAuthority grantedAuthority = new GrantedAuthority() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public String getAuthority() {
					
					String privilege = userRole.getName();   
					return privilege;
				}
			};
			roleAuthorities.add(grantedAuthority);
			
			if(subscriptionPermissions!=null && !subscriptionPermissions.isEmpty()){
				for (String subscriptionPermission : subscriptionPermissions) {

					grantedAuthority = new GrantedAuthority() {
						
						private static final long serialVersionUID = 1L;

						@Override
						public String getAuthority() {
							
							String privilege = subscriptionPermission;
							return privilege;
						}
					};
					roleAuthorities.add(grantedAuthority);
				}
			}
			
			if(subAdminPermissions!=null 
					&& !subAdminPermissions.isEmpty()){
				for (SubAdminPermission adminPermission : subAdminPermissions) {

					grantedAuthority = new GrantedAuthority() {
						
						private static final long serialVersionUID = 1L;

						@Override
						public String getAuthority() {
							
							String privilege = adminPermission.getAdminPermissions().getPermission(); 
							return privilege;
						}
					};
					roleAuthorities.add(grantedAuthority);
				}
			}
			
		}
		return roleAuthorities;
	}

	@Override
	public String getUsername() {
		return getEmailId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public List<String> getSubscriptionPermissions() {
		return subscriptionPermissions;
	}

	public void setSubscriptionPermissions(List<String> subscriptionPermissions) {
		this.subscriptionPermissions = subscriptionPermissions;
	}


	

}
