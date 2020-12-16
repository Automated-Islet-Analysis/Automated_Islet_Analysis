
mov = ('2_aligned_vid.tif');
for i=1:148
    vid{i} = (imread(mov,i));
end

%% Filter
dminus = 15;
dzero=17;
size_filter=31; % should be odd


filt = ones(size_filter);
vec = (-floor(size_filter/2) : floor(size_filter/2)).^2;
dist = sqrt(vec + vec');
filt(dist<dzero/2)=0;
filt(dist<dminus/2)=-2;
filt = filt/sum(filt(:));
%% Search
filteredImage = zeros(size(vid{1}));
coo=[];
for i=1
    i
    J=vid{i};
%     J = adapthisteq(vid{i},'NumTiles',[15 15],'ClipLimit',0.05);
    new = conv2(J, filt, 'same');
    % Find max 1
    m_=max(new(:));
    I = find(new==m_);
    x = mod(I,size(new,1));
    y = floor(I/size(new,1));
    coo = [coo;x,y];
    
    % Find max 2
    new(x-30:x+30,y-30:y+30)=0;
    m_=max(new(:));
    I = find(new==m_);
    x = mod(I,size(new,1));
    y = floor(I/size(new,1));
    coo = [coo;x,y];
    
    %Find max 3
    new(x-30:x+30,y-30:y+30)=0;
    m_=max(new(:));
    I = find(new==m_);
    x = mod(I,size(new,1));
    y = floor(I/size(new,1));
    coo = [coo;x,y];
end
%% Analyse coo
coo_uniq=coo(1,:);

for i = 2:size(coo,1)
    temp = abs(coo_uniq - coo(i,:));
    temp = sum(temp,2);
    if(min(temp)> 50)
        coo_uniq = [coo_uniq;coo(i,:)];
    end
end

%% Show
figure
imshow(vid{1})
hold on
scatter(coo_uniq(:,2),coo_uniq(:,1),'linewidth',4)


%% Plot Mean intensity results
close all
figure
n=5;
step = 1;
MI=[];
for i=(1:step:n*step) 
    filename = "MI_data/"+num2str(i)+".csv";
    Array=csvread(filename);
    MI = [MI;detrend(Array(:,2)')]; 
end
MIP = MI - mean(MI,2);
MIP = 0.5*MIP./max(abs(MIP),[],2);
MIP = MIP + (1:n)';

time_ax = (0:size(MI,2)-1)' *0.2;
plot(time_ax,MIP')
hold on
plot(time_ax,repmat(1:n,size(MI,2),1),"--k")
xlabel("time(s)")
yticks([1:n])
ylabel("Mean intensity(AU)")
    
    

